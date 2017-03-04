package de.mischok.konkretlive.registration.test.service;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import de.mischok.konkretlive.registration.test.TestConfiguration;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfiguration.class)
public class GroupRegistrationServiceTest {
	
	@Value("${local.server.port}")
	private int port; //inject dynamically assigned port
	
	/**
	 * Does the test setup
	 * @throws Exception Any exceptions are re-thrown
	 */
	@Before
	public void setUp() throws Exception {
		RestAssured.port = this.port;
	}
	
	@Test
	public void testSaveRegistration() {
		String json = "{ \"group\": { \"church\": \"FeG Augsburg-Mitte\", \"type\": \"grouptype.jugendkreis\", \"district\": \"district.suedbayern\" }, \"leader\": { \"firstname\": \"Julius\", \"lastname\": \"Mischok\", \"street\": \"Jesuitengasse 23\", \"zipcode\": \"86152\", \"city\": \"Augsburg\", \"mobile\": \"01704109941\", \"email\": \"julius.mischok@mischok-it.de\", \"birthday\": \"1986-02-17T23:00:00.000Z\", \"price\": \"price.vollverdiener\", \"vegetarian\": true }, \"participants\": [ { \"firstname\": \"Lenja\", \"lastname\": \"Mischok\", \"email\": \"lenja@mischok-it.de\", \"birthday\": \"2016-10-22T22:00:00.000Z\", \"price\": \"price.nichtverdiener\", \"foodallergy\": true, \"mobile\": \"0170/9350225\", \"medicalhints\": \"Baby\" } ] }";
		
		String location = RestAssured.given()
			.contentType(ContentType.JSON)
			.body(json)
		.when()
			.post("registration")
		.then()
			.log().all()
			.statusCode(HttpStatus.CREATED.value())
			.extract().header("Location");
		
		RestAssured.when()
			.get(location)
		.then()
			.statusCode(HttpStatus.UNAUTHORIZED.value());
		
		RestAssured.given()
			.auth().basic("dev", "dev")
		.when()
			.log().all()
			.get(location)
		.then()
			.log().all()
			.statusCode(HttpStatus.OK.value())
			.body("group.church", Matchers.equalTo("FeG Augsburg-Mitte"))
			.body("group.type", Matchers.equalTo("grouptype.jugendkreis"))
			.body("group.district", Matchers.equalTo("district.suedbayern"))
			.body("leader.firstname", Matchers.equalTo("Julius"))
			.body("leader.lastname", Matchers.equalTo("Mischok"))
			.body("leader.street", Matchers.equalTo("Jesuitengasse 23"))
			.body("leader.zipcode", Matchers.equalTo("86152"))
			.body("leader.city", Matchers.equalTo("Augsburg"))
			.body("leader.mobile", Matchers.equalTo("01704109941"))
			.body("leader.email", Matchers.equalTo("julius.mischok@mischok-it.de"))
			.body("leader.birthday", Matchers.equalTo("1986-02-17T23:00:00Z"))
			.body("leader.price", Matchers.equalTo("price.vollverdiener"))
			.body("leader.vegetarian", Matchers.equalTo(true))
			.body("participants.size()", Matchers.equalTo(1))
			.body("participants.firstname", Matchers.hasItem("Lenja"))
			.body("participants.lastname", Matchers.hasItem("Mischok"))
			.body("participants.mobile", Matchers.hasItem("0170/9350225"))
			.body("participants.email", Matchers.hasItem("lenja@mischok-it.de"))
			.body("participants.birthday", Matchers.hasItem("2016-10-22T22:00:00Z"))
			.body("participants.price", Matchers.hasItem("price.nichtverdiener"))
			.body("participants.medicalhints", Matchers.hasItem("Baby"))
			;
	}
	
	@Test
	public void testValidation() {
		String json = "{ \"group\": { \"church\": \"FeG Augsburg-Mitte\", \"type\": \"grouptype.jugendkreis\", \"district\": \"district.suedbayern\" }, \"leader\": { \"firstname\": \"Julius\", \"lastname\": \"Mischok\", \"street\": \"Jesuitengasse 23\", \"zipcode\": \"86152\", \"city\": \"Augsburg\", \"mobile\": \"01704109941\", \"email\": \"julius.mischok@mischok-it.de\", \"birthday\": \"1986-06-01T23:59:59.999Z\", \"price\": \"price.vollverdiener\", \"vegetarian\": true }, \"participants\": [ { \"firstname\": \"Lenja\", \"lastname\": \"Mischok\", \"email\": \"lenja@mischok-it.de\", \"birthday\": \"2016-10-22T22:00:00.000Z\", \"price\": \"price.nichtverdiener\", \"foodallergy\": true, \"medicalhints\": \"Baby\" } ] }";
		
		RestAssured.given()
			.contentType(ContentType.JSON)
			.body(json)
		.when()
			.post("registration")
		.then()
			.log().all()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("field", Matchers.hasItem("participants[0].mobile"))
			.body("code", Matchers.hasItem("required"))
			;
		
		json = "{ \"group\": { \"church\": \"FeG Augsburg-Mitte\", \"type\": \"grouptype.jugendkreis\", \"district\": \"district.suedbayern\" }, \"leader\": { \"firstname\": \"Julius\", \"lastname\": \"Mischok\", \"street\": \"Jesuitengasse 23\", \"zipcode\": \"86152\", \"city\": \"Augsburg\", \"mobile\": \"01704109941\", \"email\": \"julius.mischok@mischok-it.de\", \"birthday\": \"1999-06-01T23:59:59.999Z\", \"price\": \"price.vollverdiener\", \"vegetarian\": true }, \"participants\": [ { \"firstname\": \"Lenja\", \"lastname\": \"Mischok\", \"email\": \"lenja@mischok-it.de\", \"birthday\": \"2016-10-22T22:00:00.000Z\", \"price\": \"price.nichtverdiener\", \"foodallergy\": true, \"medicalhints\": \"Baby\", \"mobile\": \"habkeins\" } ] }";
		
		RestAssured.given()
			.contentType(ContentType.JSON)
			.body(json)
		.when()
			.post("registration")
		.then()
			.log().all()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("field", Matchers.hasItem("leader.birthday"))
			.body("code", Matchers.hasItem("fullage"))
			;
		
		json = "{ \"group\": { \"type\": \"grouptype.jugendkreis\", \"district\": \"district.suedbayern\" }, \"leader\": { \"firstname\": \"Julius\", \"lastname\": \"Mischok\", \"street\": \"Jesuitengasse 23\", \"zipcode\": \"86152\", \"city\": \"Augsburg\", \"mobile\": \"01704109941\", \"email\": \"julius.mischok@mischok-it.de\", \"birthday\": \"1986-02-17T23:00:00.000Z\", \"price\": \"price.vollverdiener\", \"vegetarian\": true }, \"participants\": [ { \"firstname\": \"Lenja\", \"lastname\": \"Mischok\", \"email\": \"lenja@mischok-it.de\", \"birthday\": \"2016-10-22T22:00:00.000Z\", \"price\": \"price.nichtverdiener\", \"foodallergy\": true, \"mobile\": \"0170/9350225\", \"medicalhints\": \"Baby\" } ] }";
		
		RestAssured.given()
			.contentType(ContentType.JSON)
			.body(json)
		.when()
			.post("registration")
		.then()
			.log().all()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("field", Matchers.hasItem("group.church"))
			.body("code", Matchers.hasItem("NotBlank"))
			;
	}
}
