package de.mischok.konkretlive.registration.test.mongodb;

import java.util.Arrays;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.mischok.konkretlive.registration.model.Group;
import de.mischok.konkretlive.registration.model.Leader;
import de.mischok.konkretlive.registration.model.Person;
import de.mischok.konkretlive.registration.model.Registration;
import de.mischok.konkretlive.registration.repository.GroupRepository;
import de.mischok.konkretlive.registration.repository.LeaderRepository;
import de.mischok.konkretlive.registration.repository.PersonRepository;
import de.mischok.konkretlive.registration.repository.RegistrationRepository;
import de.mischok.konkretlive.registration.test.TestConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
public class RegistrationRepositoryTest {
	
	@Autowired
	private RegistrationRepository registrationRepository;
	
	@Autowired
	private GroupRepository groupRepository;
	
	@Autowired
	private LeaderRepository leaderRepository;
	
	@Autowired
	private PersonRepository personRepository;
	
	@Test
	public void testSaveRegistration() {
		Person participant = new Person();
		participant.setBirthday(new Date());
		participant.setEmail("julius.mischok@mischok-it.de");
		participant.setFirstname("Julius");
		participant.setFoodallergy(true);
		participant.setLastname("Mischok");
		participant.setMedicalhints("Keine");
		participant.setMobile("01704109941");
		participant.setPrice("any.price");
		participant.setVegetarian(true);
		
		Leader leader = new Leader();
		leader.setBirthday(new Date());
		leader.setEmail("rebecca.mischok@mischok-it.de");
		leader.setFirstname("Rebecca");
		leader.setFoodallergy(false);
		leader.setLastname("Mischok");
		leader.setMedicalhints("Auch keine");
		leader.setMobile("0170/9350225");
		leader.setPrice("another.price");
		leader.setVegetarian(false);
		leader.setAddressextra("none");
		leader.setCity("Augsburg");
		leader.setStreet("Jesuitengasse 23");
		leader.setZipcode("86152");
		
		Group group = new Group();
		group.setChurch("Stadel");
		group.setDistrict("any.district");
		group.setType("any.type");
		
		Registration registration = new Registration();
		registration.setGroup(group);
		registration.setLeader(leader);
		registration.setParticipants(Arrays.asList(participant));
		
		registration.setGroup(this.groupRepository.save(registration.getGroup())); 
		registration.setLeader(this.leaderRepository.save(registration.getLeader()));
		for (int i=0; i<registration.getParticipants().size(); i++) {
			registration.getParticipants().set(i, this.personRepository.save(registration.getParticipants().get(i))); 
		}
		
		Registration saved = this.registrationRepository.save(registration);
		
		Assert.assertNotNull(saved.getId());
		Assert.assertNotNull(saved.getGroup());
		Assert.assertNotNull(saved.getLeader());
		Assert.assertNotNull(saved.getParticipants());
		
		Assert.assertEquals(participant.getFirstname(), saved.getParticipants().get(0).getFirstname());
		Assert.assertEquals(leader.getCity(), saved.getLeader().getCity());
		Assert.assertEquals(group.getDistrict(), saved.getGroup().getDistrict());
		
		Registration read = this.registrationRepository.findOne(saved.getId());

		Assert.assertNotNull(read.getGroup());
		Assert.assertNotNull(read.getLeader());
		Assert.assertNotNull(read.getParticipants());
		
		Assert.assertEquals(participant.getPrice(), read.getParticipants().get(0).getPrice());
		Assert.assertEquals(leader.isVegetarian(), read.getLeader().isVegetarian());
		Assert.assertEquals(group.getChurch(), read.getGroup().getChurch());
	}
}
