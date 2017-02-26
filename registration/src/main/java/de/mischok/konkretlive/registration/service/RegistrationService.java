package de.mischok.konkretlive.registration.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import de.mischok.konkretlive.registration.model.Person;
import de.mischok.konkretlive.registration.model.Registration;
import de.mischok.konkretlive.registration.repository.GroupRepository;
import de.mischok.konkretlive.registration.repository.LeaderRepository;
import de.mischok.konkretlive.registration.repository.PersonRepository;
import de.mischok.konkretlive.registration.repository.RegistrationRepository;
import jxl.Workbook;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * Service implementation 
 */
@Controller
@RequestMapping("registration")
public class RegistrationService {

	@Autowired
	private RegistrationRepository registrationRepository;
	
	@Autowired
	private GroupRepository groupRepository;
	
	@Autowired
	private LeaderRepository leaderRepository;
	
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private EmailService emailService;
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> saveRegistration(@RequestBody @Valid Registration registration, BindingResult bindingResult) {
		Assert.notNull(registration);
		Assert.notNull(bindingResult);
		
		bindingResult = this.doValidation(registration, bindingResult);
		
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
		} else {
			registration.setGroup(this.groupRepository.save(registration.getGroup())); 
			registration.setLeader(this.leaderRepository.save(registration.getLeader()));
			for (int i=0; i<registration.getParticipants().size(); i++) {
				registration.getParticipants().set(i, this.personRepository.save(registration.getParticipants().get(i))); 
			}
			
			Registration saved = this.registrationRepository.save(registration);
			
			try {
				this.sendEmail(registration);

				URI location = UriComponentsBuilder.fromPath("/")
						.pathSegment("registration")
						.pathSegment(saved.getId())
						.build().toUri();
				
				return ResponseEntity.created(location).build();
			} catch (MailException e) {
				return ResponseEntity.status(520).body("Sending confirmation email failed");
			}
		}
	}
	
	private void sendEmail(Registration registration) throws MailException {
		Assert.notNull(registration);
		
		String emailText = this.generateEmailTextLeader(registration);
		InputStreamSource attachmentStream = this.exportToExcel(registration);
		String emailTextOffice = "Liebes Office,<br/><br/>es gibt eine neue Anmeldung für konkret live. Die Daten sind dieser E-Mail angehängt.<br/><br/>Viele Grüße!";
		
		this.emailService.sendEmail(registration.getLeader().getEmail(), "Anmeldebestätigung konkret live", emailText, "anmeldedaten.xls", attachmentStream);
		// FIXME: Just for testing purposes!
		//this.emailService.sendEmail("office@konkretlive.de", "Anmeldung " + registration.getGroup().getType() + " " + registration.getGroup().getChurch(), emailTextOffice, "anmeldedaten.xls", attachmentStream);
	}

	private InputStreamSource exportToExcel(Registration registration) {
		Assert.notNull(registration);
		
		try {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(outStream);
			
			workbook = this.writeGroupSheet(registration, workbook);
			workbook = this.writeParticipants(registration, workbook);
			
			workbook.write();
			workbook.close();
			
			return new ByteArrayResource(outStream.toByteArray());
		} catch (IOException | WriteException e) {
			throw new RuntimeException(); // FIXME: Correct error message!
			// FIXME: Logging!
		}
	}

	private WritableWorkbook writeGroupSheet(Registration registration, WritableWorkbook workbook) throws WriteException {
		Assert.notNull(registration);
		Assert.notNull(workbook);
		
		WritableSheet worksheet = workbook.createSheet("Gruppendaten", 0);
		
		WritableFont boldFont = new WritableFont(WritableFont.ARIAL);
		boldFont.setBoldStyle(WritableFont.BOLD);
		
		WritableCellFormat boldCellFormat = new WritableCellFormat(boldFont);
		WritableCellFormat normalCellFormat = new WritableCellFormat(new WritableFont(WritableFont.ARIAL));
		WritableCellFormat normalDateCellFormat = new WritableCellFormat(new DateFormat("dd.MM.YYYY hh:mm:ss"));
		
		worksheet.addCell(new Label(0, 0, "Gemeinde", boldCellFormat));
		worksheet.addCell(new Label(1, 0, "Gruppenart", boldCellFormat));
		worksheet.addCell(new Label(2, 0, "Bundeskreis", boldCellFormat));
		worksheet.addCell(new Label(3, 0, "Anmeldedatum", boldCellFormat));
		
		worksheet.addCell(new Label(0, 1, registration.getGroup().getChurch(), normalCellFormat));
		worksheet.addCell(new Label(1, 1, this.getGroupType(registration.getGroup().getType()), normalCellFormat));
		worksheet.addCell(new Label(2, 1, this.getDistrict(registration.getGroup().getDistrict()), normalCellFormat));
		worksheet.addCell(new DateTime(3, 1, new Date(), normalDateCellFormat));
		
		return workbook;
	}

	private String getGroupType(String type) {
		Assert.hasText(type);
		
		switch (type) {
		case "grouptype.teenkreis":
			return "Teenkreis";
		case "grouptype.jugendkreis":
			return "Jugendkreis";
		case "grouptype.kje":
			return "KJE";
		case "grouptype.bu":
			return "BU";
		case "grouptype.sonstiges":
			return "Sonstiges";
		default:
			return type;
		}
	}

	private String getDistrict(String district) {
		Assert.hasText(district);

		switch (district) {
		case "district.suedbayern":
			return "Südbayerischer Kreis";
		case "district.nordbayern":
			return "Nordbayerischer Kreis";
		case "district.suedbawue":
			return "Baden-Württemberg Süd Kreis";
		case "district.nordbawue":
			return "Baden-Württemberg Nord Kreis";
		case "district.sonstiges":
			return "Sonstiges";
		default:
			return district;
		}
	}

	private WritableWorkbook writeParticipants(Registration registration, WritableWorkbook workbook) {
		Assert.notNull(registration);
		Assert.notNull(workbook);
		
		WritableSheet worksheet = workbook.createSheet("Teilnehmer", 1);
		
		// TODO Auto-generated method stub
		
		return workbook;
	}

	private String generateEmailTextLeader(Registration registration) {
		// TODO Auto-generated method stub
		return "TODO";
	}

	private BindingResult doValidation(Registration registration, BindingResult bindingResult) {
		Assert.notNull(registration);
		Assert.notNull(bindingResult);
		
		bindingResult = this.checkPersonFullAge(registration.getLeader(), bindingResult);
		bindingResult = this.checkPersonsMobileRequired(registration.getParticipants(), bindingResult);
		
		return bindingResult;
	}

	private BindingResult checkPersonFullAge(Person person, BindingResult bindingResult) {
		Assert.notNull(person);
		Assert.notNull(bindingResult);
		
		Calendar calendar = new GregorianCalendar();
		calendar.set(2017-18, Calendar.JUNE, 0, 23, 59, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date referenceDateInclusive = calendar.getTime();
		
		if (person.getBirthday().after(referenceDateInclusive)) {
			bindingResult.rejectValue("leader.birthday", "fullage", "Group leader has to be full aged at festival begin");
		}
		
		return bindingResult;
	}

	private BindingResult checkPersonsMobileRequired(List<Person> participants, final BindingResult bindingResult) {
		Assert.notNull(participants);
		Assert.notNull(bindingResult);
		
		for (int index=0; index<participants.size(); index++) {
			Person participant = participants.get(index);
			
			if (participant.isFoodallergy() && (participant.getMobile() == null || participant.getMobile().isEmpty())) {
				bindingResult.rejectValue("participants[" + index + "].mobile", "required", "Mobile required, if foodallergy is true");
			}
		};
		
		return bindingResult;
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public ResponseEntity<Registration> getRegistration(@PathVariable String id) {
		Assert.hasText(id);
		
		Registration registration = this.registrationRepository.findOne(id);
		
		return ResponseEntity.ok(registration);
	}
}
