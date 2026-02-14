package Automation.apitesting.tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;

import Automation.apitesting.utils.BaseTest;
import Automation.apitesting.utils.FileNameConstants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;

public class PostAPIRequestUsingFile extends BaseTest{

	@Test
	public void postAPIRequest() {

		try {
			String postAPIRequestbody=FileUtils.readFileToString(new File(FileNameConstants.POST_API_REQUEST_BODY), "UTF-8");
			
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
			
			JSONArray jsonArrayLastname = JsonPath.read(response.body().asString(),"$.booking..lastname");
			String Lastname = (String) jsonArrayLastname.get(0);
			
			Assert.assertEquals(Lastname, "boyle");
			
			JSONArray jsonArraycheckin = JsonPath.read(response.body().asString(),"$.booking.bookingdates..checkin");
			String Checkin = (String) jsonArraycheckin.get(0);
			
			Assert.assertEquals(Checkin, "2026-02-01");
			
			
			int bookingID = JsonPath.read(response.body().asString(),"$.bookingid");  // 
			
			RestAssured
				.given()
					.contentType(ContentType.JSON)
					.baseUri("https://restful-booker.herokuapp.com/booking")
				.when()
					.get("/{bookingid}",bookingID)
				.then()
					.assertThat()
					.statusCode(200);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
