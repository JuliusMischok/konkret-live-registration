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
public class StaffRegistrationServiceTest {
	
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
		String json = "{ \"firstname\": \"Julius\", \"lastname\": \"Mischok\", \"street\": \"Jesuitengasse 23\", \"zipcode\": \"86152\", \"city\": \"Augsburg\","
				+ " \"addressextra\": \"App. 22\", \"mobile\": \"sdfdsf\", \"email\": \"julius.mischok@mischok-it.de\", \"birthday\": \"2017-03-28T22:00:00.000Z\", "
				+ " \"district\": \"district.nordbayern\", \"arrival\": \"2017-05-30T22:00:00.000Z\", \"departure\": \"2017-05-31T22:00:00.000Z\", \"housing\": \"housing.bed\","
				+ " \"ou\": \"ou.logistics\", \"price\": \"58\", \"vegetarian\": true, \"foodallergy\": true, \"medicalhints\": \"Keine\", \"nickname\": \"Hottie\", \"known\": true }";
		
		String location = RestAssured.given()
			.contentType(ContentType.JSON)
			.body(json)
		.when()
			.post("registration/staff")
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
			.body("firstname", Matchers.equalTo("Julius"))
			.body("lastname", Matchers.equalTo("Mischok"))
			.body("street", Matchers.equalTo("Jesuitengasse 23"))
			.body("zipcode", Matchers.equalTo("86152"))
			.body("city", Matchers.equalTo("Augsburg"))
			.body("addressextra", Matchers.equalTo("App. 22"))
			.body("mobile", Matchers.equalTo("sdfdsf"))
			.body("email", Matchers.equalTo("julius.mischok@mischok-it.de"))
			.body("birthday", Matchers.equalTo("2017-03-28T22:00:00Z"))
			.body("district", Matchers.equalTo("district.nordbayern"))
			.body("arrival", Matchers.equalTo("2017-05-30T22:00:00Z"))
			.body("departure", Matchers.equalTo("2017-05-31T22:00:00Z"))
			.body("housing", Matchers.equalTo("housing.bed"))
			.body("ou", Matchers.equalTo("ou.logistics"))
			.body("price", Matchers.equalTo("58"))
			.body("vegetarian", Matchers.equalTo(true))
			.body("foodallergy", Matchers.equalTo(true))
			.body("medicalhints", Matchers.equalTo("Keine"))
			.body("nickname", Matchers.equalTo("Hottie"))
			.body("known", Matchers.equalTo(true))
			;
	}
}
