package Automation.apitesting.tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;

import Automation.apitesting.utils.FileNameConstants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;

public class PatchAPIRequest {

	
	@Test
	public void patchAPIRequest() {

		try {
			String postAPIRequestbody=FileUtils.readFileToString(new File(FileNameConstants.POST_API_REQUEST_BODY), "UTF-8");
			
			String tokenAPIRequestbody=FileUtils.readFileToString(new File(FileNameConstants.TOKEN_API_REQUEST_BODY), "UTF-8");
			
			String putAPIRequestbody=FileUtils.readFileToString(new File(FileNameConstants.PUT_API_REQUEST_BODY), "UTF-8");
			
			String patchAPIRequestbody=FileUtils.readFileToString(new File(FileNameConstants.PATCH_API_REQUEST_BODY), "UTF-8");
			
			
			//Post API Call
			Response response = 
			RestAssured
				.given()
					.contentType(ContentType.JSON)
					.body(postAPIRequestbody)
					.baseUri("https://restful-booker.herokuapp.com/booking")
				.when()
					.post()
				.then()
					.assertThat()
					.statusCode(200)
				.extract()
					.response();
			
			
			JSONArray jsonArray = JsonPath.read(response.body().asString(),"$.booking..firstname");
			String firstName = (String) jsonArray.get(0);
			
			Assert.assertEquals(firstName, "Philip");
			
			int bookingID = JsonPath.read(response.body().asString(),"$.bookingid");  // 
			
			
			//get API Call
			RestAssured
				.given()
					.contentType(ContentType.JSON)
					.baseUri("https://restful-booker.herokuapp.com/booking")
				.when()
					.get("/{bookingid}",bookingID)
				.then()
					.assertThat()
					.statusCode(200);
			
			//token generation
			Response tokenAPIResponse = 
			RestAssured
					.given()
						.contentType(ContentType.JSON)
						.body(tokenAPIRequestbody)
						.baseUri("https://restful-booker.herokuapp.com/auth")
					.when()
						.post()
					.then()
						.assertThat()
						.statusCode(200)
					.extract()
					.response();
			
			String token = JsonPath.read(tokenAPIResponse.body().asString(),"$.token");  // 
			
			//put API Call
			RestAssured
				.given()
					.contentType(ContentType.JSON)
					.body(putAPIRequestbody)
					.header("Cookie","token="+token)
					.baseUri("https://restful-booker.herokuapp.com/booking")
				.when()
					.put("{bookingID}",bookingID)
				.then()
					.assertThat()
					.statusCode(200)
					.body("firstname", Matchers.equalTo("Michele"))
					.body("lastname", Matchers.equalTo("Bankole"));
			
			//patch api Call
			RestAssured
				.given()
					.contentType(ContentType.JSON)
					.body(patchAPIRequestbody)
					.header("Cookie","token="+token)
					.baseUri("https://restful-booker.herokuapp.com/booking")
				.when()
					.patch("{bookingID}",bookingID)
				.then()
					.assertThat()
					.statusCode(200)
					.body("firstname", Matchers.equalTo("Sabrina"));
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
}
