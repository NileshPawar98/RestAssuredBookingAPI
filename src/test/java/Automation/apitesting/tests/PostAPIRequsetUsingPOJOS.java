package Automation.apitesting.tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Automation.apitesting.utils.FileNameConstants;
import POJOS.APITesting.Booking;
import POJOS.APITesting.BookingDates;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class PostAPIRequsetUsingPOJOS {

	@Test
	public void postAPIRequest()
	{
		try {
			
			String jsonSchema = FileUtils.readFileToString(new File(FileNameConstants.JSON_SCHEMA),"UTF-8");
			
			BookingDates bookingdates = new BookingDates("2026-02-01", "2026-02-05");
			
			Booking booking = new Booking("Philip", "boyle","brakefast",1000,true,bookingdates);
			
			ObjectMapper objectMapper  = new ObjectMapper();
			String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(booking);
			
			System.out.println(requestBody);
			
			//de-Serialization
			Booking bookingDetails = objectMapper.readValue(requestBody, Booking.class);
			System.out.println(bookingDetails.getFirstname());
			System.out.println(bookingDetails.getTotalprice());
			
			System.out.println(bookingDetails.getBookingdates().getCheckin());
			System.out.println(bookingDetails.getBookingdates().getCheckout());
			
			Response response = 
			RestAssured
				.given()
					.contentType(ContentType.JSON)
					.body(requestBody)
					.baseUri("https://restful-booker.herokuapp.com/booking")
				.when()
					.post()
				.then()
					.assertThat()
					.statusCode(200)
				.extract()
					.response();
		int bookingId = response.path("bookingid");
		
		System.out.println(jsonSchema);
		
		
		RestAssured
			    .given()
			        .contentType(ContentType.JSON)
			        .baseUri("https://restful-booker.herokuapp.com/booking")
			    .when()
			        .get("/{bookingid}",bookingId)
			    .then()
			        .assertThat()
			        .statusCode(200)
					.body(JsonSchemaValidator.matchesJsonSchema(jsonSchema));
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
