package com.npwit.test.eureka;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.npwit.eureka.EurekaServerApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EurekaServerApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("standalone")
public class EurekaServerApplicationTest {

	@LocalServerPort
	private int port;

	@Before
	public void setup() {
		RestAssured.port = this.port;
	}

	@Test
	public void shouldRetrieveHomePage() {
		RestAssured.
			given().
				accept(ContentType.JSON).
			when().
				get("/").
			then().
				statusCode(HttpStatus.SC_OK).
				contentType(ContentType.HTML);
	}
}