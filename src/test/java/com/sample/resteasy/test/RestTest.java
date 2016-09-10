package com.sample.resteasy.test;

import org.jboss.resteasy.plugins.server.netty.NettyJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.jayway.restassured.http.ContentType;
import com.sample.resteasy.app.RestEasyApp;
import com.sample.resteasy.pojo.City;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

/**
 * RESTFul Webservices testing using TestNG,Rest-assured and Netty
 * 
 * @author manjunathshetty
 *
 */
@Test(groups = "rest-tests", alwaysRun = true)
public class RestTest {

	private NettyJaxrsServer server;

	@BeforeTest
	public void init() {
		server = new NettyJaxrsServer();
		ResteasyDeployment deployment = new ResteasyDeployment();
		deployment.setApplicationClass(RestEasyApp.class.getName());
		server.setDeployment(deployment);
		server.setRootResourcePath("/ws");
		server.setPort(4321);
		server.start();
	}

	@Test(enabled = true)
	public void helloworldWSTest() {
		when().get("http://localhost:4321/ws/sample").then().statusCode(200).body(is("Hello World!"));
	}

	@Test(enabled = true)
	public void getJsonTest() {
		when().get("http://localhost:4321/ws/sample/city").then().statusCode(200).body("name", equalTo("Bengaluru"),
				"knownFor", equalTo("IT"));
	}

	@Test(enabled = true)
	public void postJsonTest() {
		City city = new City();
		city.setName("Mumbai");
		city.setKnownFor("Business");
		given().contentType(ContentType.JSON).content(city).when().post("http://localhost:4321/ws/sample/city").then()
				.statusCode(200);
	}

	@AfterTest
	public void clear() {
		if (server != null)
			server.stop();
	}

}
