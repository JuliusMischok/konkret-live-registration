package de.mischok.konkretlive.registration.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.validation.Valid;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.MailPreparationException;
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
import de.mischok.konkretlive.registration.model.Staff;
import de.mischok.konkretlive.registration.repository.GroupRepository;
import de.mischok.konkretlive.registration.repository.LeaderRepository;
import de.mischok.konkretlive.registration.repository.PersonRepository;
import de.mischok.konkretlive.registration.repository.RegistrationRepository;
import de.mischok.konkretlive.registration.repository.StaffRepository;
import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

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
	private StaffRepository staffRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Value("${mail.office}")
	private String officeAddress;
	
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
				e.printStackTrace();
				return ResponseEntity.status(520).body("Sending confirmation email failed");
			}
		}
	}
	
	private void sendEmail(Registration registration) throws MailException {
		Assert.notNull(registration);
		
		try {
			String emailText = this.generateEmailTextLeader(registration);
			InputStreamSource attachmentStream = this.exportToExcel(registration);
			
			String emailTextOffice = "Liebes Office,<br/><br/>es gibt eine neue Anmeldung für konkret live. Die Daten sind dieser E-Mail angehängt.<br/><br/>Viele Grüße!";
			
			this.emailService.sendEmail(registration.getLeader().getEmail(), "Anmeldebestätigung konkret live", emailText, "anmeldedaten.xls", attachmentStream);
			this.emailService.sendEmail(this.officeAddress, "Anmeldung " + this.getGroupType(registration.getGroup().getType()) + " " + registration.getGroup().getChurch(), emailTextOffice, "anmeldedaten.xls", attachmentStream);
			
		} catch (WriteException | IOException e) {
			throw new MailPreparationException("Error creating excel file", e);
		}
	}

	private InputStreamSource exportToExcel(Registration registration) throws IOException, WriteException {
		Assert.notNull(registration);
		
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		WritableWorkbook workbook = Workbook.createWorkbook(outStream);
		
		workbook = this.writeGroupSheet(registration, workbook);
		workbook = this.writeParticipants(registration, workbook);
		
		workbook.write();
		workbook.close();
		
		return new ByteArrayResource(outStream.toByteArray());
	}

	private WritableWorkbook writeGroupSheet(Registration registration, WritableWorkbook workbook) throws WriteException {
		Assert.notNull(registration);
		Assert.notNull(workbook);
		
		WritableSheet worksheet = workbook.createSheet("Gruppendaten", 0);
		
		worksheet.addCell(new Label(0, 0, "Gemeinde", this.getBoldCellFormat()));
		worksheet.addCell(new Label(1, 0, "Gruppenart", this.getBoldCellFormat()));
		worksheet.addCell(new Label(2, 0, "Bundeskreis", this.getBoldCellFormat()));
		worksheet.addCell(new Label(3, 0, "Anmeldedatum", this.getBoldCellFormat()));
		worksheet.addCell(new Label(4, 0, "Gesamtpreis", this.getBoldCellFormat()));
		
		worksheet.addCell(new Label(0, 1, registration.getGroup().getChurch(), this.getNormalCellFormat()));
		worksheet.addCell(new Label(1, 1, this.getGroupType(registration.getGroup().getType()), this.getNormalCellFormat()));
		worksheet.addCell(new Label(2, 1, this.getDistrict(registration.getGroup().getDistrict()), this.getNormalCellFormat()));
		worksheet.addCell(new DateTime(3, 1, new Date(), this.getNormalDateTimeCell()));
		worksheet.addCell(new jxl.write.Number(4, 1, this.getWholePrice(registration), this.getNormalCurrencyFormat()));
		
		return workbook;
	}

	private WritableFont getBoldFont() throws WriteException {
		WritableFont boldFont = new WritableFont(WritableFont.ARIAL);
		boldFont.setBoldStyle(WritableFont.BOLD);
		
		return boldFont;
	}
	
	private WritableCellFormat getBoldCellFormat() throws WriteException {
		return new WritableCellFormat(this.getBoldFont());
	}
	
	private WritableCellFormat getNormalCellFormat() {
		return new WritableCellFormat(new WritableFont(WritableFont.ARIAL));
	}
	
	private WritableCellFormat getNormalDateCell() {
		return new WritableCellFormat(new DateFormat("dd.MM.YYYY"));
	}
	
	private WritableCellFormat getNormalDateTimeCell() {
		return new WritableCellFormat(new DateFormat("dd.MM.YYYY hh:mm:ss"));
	}

	private CellFormat getNormalCurrencyFormat() {
		return new WritableCellFormat(NumberFormats.ACCOUNTING_FLOAT);
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

	private WritableWorkbook writeParticipants(Registration registration, WritableWorkbook workbook) throws WriteException {
		Assert.notNull(registration);
		Assert.notNull(workbook);
		
		WritableSheet worksheet = workbook.createSheet("Teilnehmer", 1);
		
		worksheet.addCell(new Label(0, 0, "Vorname", this.getBoldCellFormat()));
		worksheet.addCell(new Label(1, 0, "Nachname", this.getBoldCellFormat()));
		worksheet.addCell(new Label(2, 0, "E-Mail", this.getBoldCellFormat()));
		worksheet.addCell(new Label(3, 0, "Mobil", this.getBoldCellFormat()));
		worksheet.addCell(new Label(4, 0, "Geburtsdatum", this.getBoldCellFormat()));
		worksheet.addCell(new Label(5, 0, "Preisgruppe", this.getBoldCellFormat()));
		worksheet.addCell(new Label(6, 0, "Preis", this.getBoldCellFormat()));
		worksheet.addCell(new Label(7, 0, "Vegetarier", this.getBoldCellFormat()));
		worksheet.addCell(new Label(8, 0, "LM-Allergien", this.getBoldCellFormat()));
		worksheet.addCell(new Label(9, 0, "Medizinische Hinweise", this.getBoldCellFormat()));
		worksheet.addCell(new Label(10, 0, "Straße Nr.", this.getBoldCellFormat()));
		worksheet.addCell(new Label(11, 0, "PLZ", this.getBoldCellFormat()));
		worksheet.addCell(new Label(12, 0, "Ort", this.getBoldCellFormat()));
		worksheet.addCell(new Label(13, 0, "Adresszusatz", this.getBoldCellFormat()));
		worksheet.addCell(new Label(14, 0, "B-Team Seelsorge", this.getBoldCellFormat()));
		worksheet.addCell(new Label(15, 0, "Gemeinde", this.getBoldCellFormat()));
		worksheet.addCell(new Label(16, 0, "Gruppenart", this.getBoldCellFormat()));
		worksheet.addCell(new Label(17, 0, "Bundeskreis", this.getBoldCellFormat()));
		worksheet.addCell(new Label(18, 0, "Anmeldedatum", this.getBoldCellFormat()));
		worksheet.addCell(new Label(19, 0, "Art", this.getBoldCellFormat()));
		
		// Leader
		worksheet.addCell(new Label(0, 1, registration.getLeader().getFirstname(), this.getNormalCellFormat()));
		worksheet.addCell(new Label(1, 1, registration.getLeader().getLastname(), this.getNormalCellFormat()));
		worksheet.addCell(new Label(2, 1, registration.getLeader().getEmail(), this.getNormalCellFormat()));
		worksheet.addCell(new Label(3, 1, registration.getLeader().getMobile(), this.getNormalCellFormat()));
		worksheet.addCell(new DateTime(4, 1, registration.getLeader().getBirthday(), this.getNormalDateCell()));
		worksheet.addCell(new Label(5, 1, this.getPriceGroupString(registration.getLeader().getPrice()), this.getNormalCellFormat()));
		worksheet.addCell(new jxl.write.Number(6, 1, this.getPrice(registration.getLeader().getPrice()), this.getNormalCurrencyFormat()));
		worksheet.addCell(new Label(7, 1, registration.getLeader().isVegetarian() ? "1" : "", this.getNormalCellFormat()));
		worksheet.addCell(new Label(8, 1, registration.getLeader().isFoodallergy() ? "1" : "", this.getNormalCellFormat()));
		worksheet.addCell(new Label(9, 1, registration.getLeader().getMedicalhints(), this.getNormalCellFormat()));
		worksheet.addCell(new Label(10, 1, registration.getLeader().getStreet(), this.getNormalCellFormat()));
		worksheet.addCell(new Label(11, 1, registration.getLeader().getZipcode(), this.getNormalCellFormat()));
		worksheet.addCell(new Label(12, 1, registration.getLeader().getCity(), this.getNormalCellFormat()));
		worksheet.addCell(new Label(13, 1, registration.getLeader().getAddressextra(), this.getNormalCellFormat()));
		worksheet.addCell(new Label(14, 1, registration.getLeader().isCounselling() ? "1" : "", this.getNormalCellFormat()));
		worksheet.addCell(new Label(15, 1, registration.getGroup().getChurch(), this.getNormalCellFormat()));
		worksheet.addCell(new Label(16, 1, this.getGroupType(registration.getGroup().getType()), this.getNormalCellFormat()));
		worksheet.addCell(new Label(17, 1, this.getDistrict(registration.getGroup().getDistrict()), this.getNormalCellFormat()));
		worksheet.addCell(new DateTime(18, 1, new Date(), this.getNormalDateTimeCell()));
		worksheet.addCell(new Label(19, 1, "GL", this.getNormalCellFormat()));
		
		// Participants
		for (int i=0; i<registration.getParticipants().size(); i++) {
			worksheet.addCell(new Label(0, 2+i, registration.getParticipants().get(i).getFirstname(), this.getNormalCellFormat()));
			worksheet.addCell(new Label(1, 2+i, registration.getParticipants().get(i).getLastname(), this.getNormalCellFormat()));
			worksheet.addCell(new Label(2, 2+i, registration.getParticipants().get(i).getEmail(), this.getNormalCellFormat()));
			worksheet.addCell(new Label(3, 2+i, registration.getParticipants().get(i).getMobile(), this.getNormalCellFormat()));
			worksheet.addCell(new DateTime(4, 2+i, registration.getParticipants().get(i).getBirthday(), this.getNormalDateCell()));
			worksheet.addCell(new Label(5, 2+i, this.getPriceGroupString(registration.getParticipants().get(i).getPrice()), this.getNormalCellFormat()));
			worksheet.addCell(new jxl.write.Number(6, 2+i, this.getPrice(registration.getParticipants().get(i).getPrice()), this.getNormalCurrencyFormat()));
			worksheet.addCell(new Label(7, 2+i, registration.getParticipants().get(i).isVegetarian() ? "1" : "", this.getNormalCellFormat()));
			worksheet.addCell(new Label(8, 2+i, registration.getParticipants().get(i).isFoodallergy() ? "1" : "", this.getNormalCellFormat()));
			worksheet.addCell(new Label(9, 2+i, registration.getParticipants().get(i).getMedicalhints(), this.getNormalCellFormat()));
			worksheet.addCell(new Label(10, 2+i, "", this.getNormalCellFormat()));
			worksheet.addCell(new Label(11, 2+i, "", this.getNormalCellFormat()));
			worksheet.addCell(new Label(12, 2+i, "", this.getNormalCellFormat()));
			worksheet.addCell(new Label(13, 2+i, "", this.getNormalCellFormat()));
			worksheet.addCell(new Label(14, 2+i, "", this.getNormalCellFormat()));
			worksheet.addCell(new Label(15, 2+i, registration.getGroup().getChurch(), this.getNormalCellFormat()));
			worksheet.addCell(new Label(16, 2+i, this.getGroupType(registration.getGroup().getType()), this.getNormalCellFormat()));
			worksheet.addCell(new Label(17, 2+i, this.getDistrict(registration.getGroup().getDistrict()), this.getNormalCellFormat()));
			worksheet.addCell(new DateTime(18, 2+i, new Date(), this.getNormalDateTimeCell()));
			worksheet.addCell(new Label(19, 2+i, "TN", this.getNormalCellFormat()));
		}
		
		return workbook;
	}

	private String getPriceGroupString(String price) {
		Assert.hasText(price);
		
		switch (price) {
		case "price.nichtverdiener":
			return "Nichtverdiener (früh)";
		case "price.geringverdiener":
			return "Geringverdiener (früh)";
		case "price.vollverdiener":
			return "Vollverdiener (früh)";
		case "price.nichtverdiener.spaet":
			return "Nichtverdiener";
		case "price.geringverdiener.spaet":
			return "Geringverdiener";
		case "price.vollverdiener.spaet":
			return "Vollverdiener";
		default:
			return price;	
		}
	}

	private double getPrice(String price) {
		Assert.hasText(price);
		
		switch (price) {
		case "price.nichtverdiener":
			return 89d;
		case "price.geringverdiener":
			return 99d;
		case "price.vollverdiener":
			return 109d;
		case "price.nichtverdiener.spaet":
			return 99d;
		case "price.geringverdiener.spaet":
			return 109d;
		case "price.vollverdiener.spaet":
			return 119d;
		default:
			return 999d;	
		}
	}

	private String generateEmailTextLeader(Registration registration) {
		Assert.notNull(registration);
		
		String result = "Hallo " + registration.getLeader().getFirstname() + ",<br/><br/>"
				+ "vielen Dank für deine Anmeldung! Wir freuen uns, dass du mit deiner Gruppe bei konkret live dabei sein wirst!<br/><br/>"
				+ "Im Anhang findest du eine Übersicht der Daten eurer Anmeldung.<br/><br/>"
				+ "Bitte überweise den gesamten Teilnehmerbeitrag in Höhe von " + new DecimalFormat("#.00").format(this.getWholePrice(registration)) + "&euro; auf das folgende Konto:<br/><br/>"
				+ "<strong>IBAN:</strong> DE42 4526 0475 0015 3744 00<br/>"
				+ "<strong>BIC:</strong> GENODEM1BFG<br/>"
				+ "<strong>Bank:</strong> SKB Witten<br/>"
				+ "<strong>Kontoinhaber:</strong> FeG Würzburg - Zeltlagerarbeit-<br/>"
				+ "<strong>Verwendungszweck:</strong> Teilnehmerbeitrag " + this.getGroupType(registration.getGroup().getType()) + " " + registration.getGroup().getChurch() +"<br/><br/>"
				+ "Bei Fragen oder Änderungen eurer Anmeldungen antworte einfach auf diese E-Mail.<br/><br/>"
				+ "Viele Grüße,<br/><br/>konkret live Office";
				
		return result;
	}

	private double getWholePrice(Registration registration) {
		Assert.notNull(registration);
		
		List<Person> persons = new ArrayList<>(registration.getParticipants());
		persons.add(registration.getLeader());
		
		return persons.stream()
				.mapToDouble(person -> this.getPrice(person.getPrice()))
				.sum();
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
		calendar.set(2017-18, Calendar.JUNE, 2, 21, 59, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date referenceDateInclusive = calendar.getTime();
		
		LoggerFactory.getLogger(this.getClass()).info("Checking full age. Person birthday: " + person.getBirthday() + ", referenceDateInclusive: " + referenceDateInclusive);
		
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
	
	@RequestMapping(value = "staff", method = RequestMethod.POST)
	public ResponseEntity<?> saveStaffRegistration(@RequestBody @Valid Staff staff, BindingResult bindingResult) {
		Assert.notNull(staff);
		Assert.notNull(bindingResult);
		
		bindingResult = this.doValidation(staff, bindingResult);
		
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
		} else {
			Staff saved = this.staffRepository.save(staff);
			
			try {
				this.sendEmail(saved);

				URI location = UriComponentsBuilder.fromPath("/")
						.pathSegment("registration/staff")
						.pathSegment(saved.getId())
						.build().toUri();
				
				return ResponseEntity.created(location).build();
			} catch (MailException e) {
				e.printStackTrace();
				return ResponseEntity.status(520).body("Sending confirmation email failed");
			}
		}
	}

	private BindingResult doValidation(Staff staff, BindingResult bindingResult) {
		return bindingResult;
	}

	private void sendEmail(Staff saved) {
		Assert.notNull(saved);
		
		try {
			String emailTextStaff = this.generateEmailTextStaff(saved);
			InputStreamSource attachmentStream = this.exportStaffToExcel(saved);
			
			String emailTextOffice = "Liebes Office,<br/><br/>es gibt eine neue Mitarbeiteranmeldung für konkret live. Die Daten sind dieser E-Mail angehängt.<br/><br/>Viele Grüße!";
			
			this.emailService.sendEmail(saved.getEmail(), "Anmeldebestätigung konkret live", emailTextStaff, "anmeldedaten.xls", attachmentStream);
			this.emailService.sendEmail(this.officeAddress, "Mitarbeiteranmeldung " + saved.getFirstname() + " " + saved.getLastname(), emailTextOffice, "anmeldedaten.xls", attachmentStream);
			
		} catch (WriteException | IOException e) {
			throw new MailPreparationException("Error creating excel file", e);
		}
	}
	
	private String generateEmailTextStaff(Staff saved) {
		Assert.notNull(saved);
		
		String address = saved.getStreet() + (saved.getAddressextra() != null && !saved.getAddressextra().isEmpty() ? "(" + saved.getAddressextra() + "), " : ", ") + saved.getZipcode() + " " + saved.getCity(); 
			
		String protectionHeadline = "<h2>INITIATIVE ZUM SCHUTZ VOR GEWALT UND MISSBRAUCH</h2>";
		String protectionKnownStaff = "<p>"
				+ "Aufgrund deiner Mitarbeit beim „BuJu 2015“ oder einer Kinder- und/oder Jugendfreizeit von „Aufwind“ seit dem Jahr 2015 musst du kein erweitertes Führungszeugnis vorlegen. Bitte beachte dennoch die übrigen, unten aufgeführten Schritte."
				+ "</p>";
		String protection = "<p>"
				+ "Im Zusammenhang mit verantwortungsbewusster Aufgabenerfüllung haben wir uns als Gesamtleitung dafür entschieden, dass wir die Initiative zum Schutz vor Gewalt und Missbrauch des Bundes Freier evangelischer Gemeinden unterstützen und erwarten, dass sich alle Mitarbeitenden entsprechend dieser Maßstäbe verhalten. Wir möchten dich darauf hinweisen, dass wir dies zum Schutz unserer Mitarbeiterenden und Teilnehmenden und unseres Festivals tun und dass diese Entscheidung NICHT in einem Misstrauen gegenüber dir als einzelne/n Mitarbeiter/in begründet ist. Für Rückfragen wende dich bitte an Lisa Plaum (mail: lisa.plaum@konkretlive.de mobil: 0160 457 60 99)."
				+ "Aufgrund dessen muss jede/r Mitarbeiter/in das Dokument"
				+ "<ul>"
				+ "<li>"
				+ "<a href=\"https://anmeldung.konkretlive.de/uploads/Anlage1_VerhaltenskodexZumSchutzVorMissbrauchUndGewalt.pdf\">Anlage1_VerhaltenskodexZumSchutzVorMissbrauchUndGewalt.pdf</a> aufmerksam lesen und unterschreiben."
				+ "</li>"
				+ "<li>"
				+ "Das Original des erweiterten Führungszeugnisses beantragen.<br/>"
				+ "Dies ist nur persönlich bei der Ortspolizeibehörde (in der Regel ist diese im Ordnungsamt des Rathauses) deines Erstwohnsitzes erhältlich. Dort legst du die ausgedruckte und ausgefüllte <a href=\"https://anmeldung.konkretlive.de/uploads/Anlage2_BescheinigungZurBeantragungDesErweitertenFührungszeugnisses.pdf\">Anlage2_BescheinigungZurBeantragungDesErweitertenFührungszeugnisses.pdf</a> vor. Das Führungszeugnis darf zu Festivalbeginn nicht älter als 3 Monate sein. Für Ehrenamtliche ist das erweiterte Führungszeugnis gratis, Hauptamtliche zahlen ca. 15,- Euro."
				+ "</li>"
				+ "<li>"
				+ "<a href=\"https://anmeldung.konkretlive.de/uploads/Anlage3_StraftatenKinder-UndJugendhilfegesetz.pdf\">Anlage3_StraftatenKinder-UndJugendhilfegesetz.pdf</a> aufmerksam lesen."
				+ "</li>"
				+ "Führungszeugnis und Verhaltenskodex zum Schutz vor Missbrauch und Gewalt müssen in Originalversion (nicht digital!) am Samstag, 20.05.2017 bei Lisa Plaum (Jahnstrasse 49-53, 35716 Dietzhölztal) vorliegen, sonst ist die Anmeldung als Mitarbeitender nicht gültig. Kannst du diesen Termin nicht einhalten, möchtest aber trotzdem teilnehmen, melde dich unbedingt persönlich bei Lisa Plaum (mail lisa.plaum@konkretlive.de mobil: 0160 457 60 99) um Weiteres zu besprechen."
				+ "</p>";
		
		String name = (saved.getNickname() != null && !saved.getNickname().isEmpty()) ? saved.getNickname() : saved.getFirstname();
		String result = "Hallo " + name + ",<br/><br/>vielen Dank für deine Anmeldung!<br/>Bitte lies die untenstehenden Hinweise aufmerksam und melde dich zeitnah bei Rückfragen.<br/><br/>Viele Grüße,<br/><br/>konkret live Office";
		
		if (saved.getAdmission() > 0 ) {
			DecimalFormat format = new DecimalFormat("#.00");
			String admissionHint = "<h2>MITARBEITERBEITRAG</h2> "
					+ "Bitte überweise den Betrag in Höhe von " + format.format(saved.getAdmission()) + " € auf eines der untenstehende Konten:<br/><br/>"
					+ "<table border=\"0\"><tr><td>"
					+ "<h3>ALS SPENDE</h3>"
					+ "Du erhältst eine Spendenquittung<br/><br/>"
					+ "<strong>Empfänger:</strong> Nordbayerische Kreis der Freien evangelischen Gemeinden<br/>"
					+ "<strong>IBAN:</strong> DE84 4526 0475 0008 3328 02<br/>"
					+ "<strong>BIC:</strong> GENODEM1BFG<br/>"
					+ "<strong>Bank:</strong> Spar- u Kreditbank des Bundes Freien evangelischen Gemeinden<br/>"
					+ "<strong>Verwendungszweck:</strong> Spende, " + saved.getFirstname() + " " + saved.getLastname() + ", " + address + "<br/>"
					+ "</td>"
					+ "<td>"
					+ "<h3>ALTERNATIV ALS BEITRAGSGEBÜHR</h3>"
					+ "Du erhältst keine Spendenquittung<br/><br/>"
					+ "<strong>IBAN:</strong> DE42 4526 0475 0015 3744 00<br/>"
					+ "<strong>BIC:</strong> GENODEM1BFG<br/>"
					+ "<strong>Bank:</strong> SKB Witten<br/>"
					+ "<strong>Kontoinhaber:</strong> FeG Würzburg - Zeltlagerarbeit-<br/>"
					+ "<strong>Verwendungszweck:</strong> Mitarbeiterbeitrag " + saved.getFirstname() + " " + saved.getLastname() + "<br/>"
					+ "</td></tr></table>";
		
			result += admissionHint;
		}
		
		result += protectionHeadline;
		
		if (saved.isKnown()) {
			result += protectionKnownStaff;
		}
		
		result += protection;
		
		return result;
	}

	private InputStreamSource exportStaffToExcel(Staff saved) throws RowsExceededException, WriteException, IOException {
		Assert.notNull(saved);
		
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		WritableWorkbook workbook = Workbook.createWorkbook(outStream);
		
		WritableSheet worksheet = workbook.createSheet("Mitarbeiterdaten", 0);
		
		worksheet.addCell(new Label(0, 0, "Vorname", this.getBoldCellFormat()));
		worksheet.addCell(new Label(1, 0, "Nachname", this.getBoldCellFormat()));
		worksheet.addCell(new Label(2, 0, "Straße Nr.", this.getBoldCellFormat()));
		worksheet.addCell(new Label(3, 0, "PLZ", this.getBoldCellFormat()));
		worksheet.addCell(new Label(4, 0, "Ort", this.getBoldCellFormat()));
		worksheet.addCell(new Label(5, 0, "Adresszusatz", this.getBoldCellFormat()));
		worksheet.addCell(new Label(6, 0, "Handy", this.getBoldCellFormat()));
		worksheet.addCell(new Label(7, 0, "E-Mail", this.getBoldCellFormat()));
		worksheet.addCell(new Label(8, 0, "Geburtsdatum", this.getBoldCellFormat()));
		worksheet.addCell(new Label(9, 0, "Vegetarier", this.getBoldCellFormat()));
		worksheet.addCell(new Label(10, 0, "LM-Allergien", this.getBoldCellFormat()));
		worksheet.addCell(new Label(11, 0, "Medizinische Hinweise", this.getBoldCellFormat()));
		worksheet.addCell(new Label(12, 0, "Bundeskreis", this.getBoldCellFormat()));
		worksheet.addCell(new Label(13, 0, "Spitzname", this.getBoldCellFormat()));
		worksheet.addCell(new Label(14, 0, "Anreise", this.getBoldCellFormat()));
		worksheet.addCell(new Label(15, 0, "Abreise", this.getBoldCellFormat()));
		worksheet.addCell(new Label(16, 0, "Unterbringung", this.getBoldCellFormat()));
		worksheet.addCell(new Label(17, 0, "Bereich", this.getBoldCellFormat()));
		worksheet.addCell(new Label(18, 0, "MA-Beitrag", this.getBoldCellFormat()));
		worksheet.addCell(new Label(19, 0, "T-Shirt", this.getBoldCellFormat()));
		worksheet.addCell(new Label(20, 0, "FZ liegt vor", this.getBoldCellFormat()));
		worksheet.addCell(new Label(21, 0, "Anmeldedatum", this.getBoldCellFormat()));
		
		worksheet.addCell(new Label(0, 1, saved.getFirstname(), this.getNormalCellFormat()));
		worksheet.addCell(new Label(1, 1, saved.getLastname(), this.getNormalCellFormat()));
		worksheet.addCell(new Label(2, 1, saved.getStreet(), this.getNormalCellFormat()));
		worksheet.addCell(new Label(3, 1, saved.getZipcode(), this.getNormalCellFormat()));
		worksheet.addCell(new Label(4, 1, saved.getCity(), this.getNormalCellFormat()));
		worksheet.addCell(new Label(5, 1, saved.getAddressextra(), this.getNormalCellFormat()));
		worksheet.addCell(new Label(6, 1, saved.getMobile(), this.getNormalCellFormat()));
		worksheet.addCell(new Label(7, 1, saved.getEmail(), this.getNormalCellFormat()));
		worksheet.addCell(new DateTime(8, 1, saved.getBirthday(), this.getNormalDateCell()));
		worksheet.addCell(new Label(9, 1, saved.isVegetarian() ? "1" : "", this.getNormalCellFormat()));
		worksheet.addCell(new Label(10, 1, saved.isVegetarian() ? "1" : "", this.getNormalCellFormat()));
		worksheet.addCell(new Label(11, 1, saved.getMedicalhints(), this.getNormalCellFormat()));
		worksheet.addCell(new Label(12, 1, this.getDistrict(saved.getDistrict()), this.getNormalCellFormat()));
		worksheet.addCell(new Label(13, 1, saved.getNickname(), this.getNormalCellFormat()));
		worksheet.addCell(new DateTime(14, 1, saved.getArrival(), this.getNormalDateTimeCell()));
		worksheet.addCell(new DateTime(15, 1, saved.getDeparture(), this.getNormalDateTimeCell()));
		worksheet.addCell(new Label(16, 1, this.getHousing(saved.getHousing()), this.getNormalCellFormat()));
		worksheet.addCell(new Label(17, 1, this.getOu(saved.getOu()), this.getNormalCellFormat()));
		worksheet.addCell(new jxl.write.Number(18, 1, saved.getAdmission(), this.getNormalCurrencyFormat()));
		worksheet.addCell(new Label(19, 1, saved.getShirtsize(), this.getNormalCellFormat()));
		worksheet.addCell(new Label(20, 1, saved.isKnown() ? "1" : "", this.getNormalCellFormat()));
		worksheet.addCell(new DateTime(21, 1, new Date(), this.getNormalDateTimeCell()));
		
		workbook.write();
		workbook.close();
		
		return new ByteArrayResource(outStream.toByteArray());
	}

	private String getHousing(String housing) {
		Assert.hasText(housing);
		
		switch (housing) {
		case "housing.self":
			return "Selbst";
		case "housing.tent":
			return "Zelt";
		case "housing.bed":
			return "Bett";
		case "housing.other":
			return "Sonstiges";
		default:
			return housing;	
		}
	}

	private String getOu(String ou) {
		Assert.hasText(ou);
		
		switch (ou) {
		case "ou.decentral":
			return "Dezentrales Programm";
		case "ou.general":
			return "Gesamtleitung";
		case "ou.hosting":
			return "Hosting";
		case "ou.logistics":
			return "Logistik";
		case "ou.staffservice":
			return "Mitarbeiter-Service";
		case "ou.pr":
			return "Öffentlichkeitsarbeit";
		case "ou.office":
			return "Office";
		case "ou.counseling":
			return "Seelsorge";
		case "ou.soundandlight":
			return "Technik";
		case "ou.central":
			return "Zentrales Programm";
		case "ou.other":
			return "Sonstiges";
		default:
			return ou;
		}
	}

	@RequestMapping(value = "staff/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getStaffRegistration(@PathVariable String id) {
		Assert.hasText(id);
		
		Staff staff = this.staffRepository.findOne(id);
		
		if (staff != null) {
			return ResponseEntity.ok(staff);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
