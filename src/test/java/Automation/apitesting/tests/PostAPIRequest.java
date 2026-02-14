package Automation.apitesting.tests;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.Test;

import Automation.apitesting.utils.BaseTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class PostAPIRequest extends BaseTest {

	@Test
	public void createBooking()
	
	{
		//Prepare request body
		JSONObject booking = new JSONObject();
		JSONObject bookingDates = new JSONObject();
		
		booking.put("firstname", "Philip");
		booking.put("lastname", "boyle");
		booking.put("totalprice", 1000);
		booking.put("depositpaid", true);
		booking.put("additionalneeds", "Breakfast");
		booking.put("bookingdates", bookingDates);
		
		bookingDates.put("checkin", "2026-02-01");
		bookingDates.put("checkout", "2026-02-05");
		
		Response responce = 
		RestAssured
			.given()
				.contentType(ContentType.JSON)
				.body(booking.toString())
				.baseUri("https://restful-booker.herokuapp.com/booking")
				//.log().all()
				//.log().body()
				//.log().headers()
			.when()
				.post()
			.then()	
				.assertThat()
				.statusCode(200)
				.log().ifValidationFails()
				//.log().all()
				//.log().body()
				//.log().headers()
				.body("booking.firstname", Matchers.equalTo("Philip"))
				.body("booking.totalprice", Matchers.equalTo(1000))
				.body("booking.bookingdates.checkin", Matchers.equalTo("2026-02-01"))
			.extract()
				.response();
		
	int bookingID = responce.path("bookingid");
			
	RestAssured
			.given()
				.contentType(ContentType.JSON)
				.pathParam("bookingID", bookingID)
				.baseUri("https://restful-booker.herokuapp.com/booking")
			.when()	
				.get("{bookingID}")
			.then()
				.assertThat()
				.statusCode(200)
				.body("firstname", Matchers.equalTo("Philip"))
				.body("lastname", Matchers.equalTo("boyle"))
				.body("totalprice", Matchers.equalTo(1000))
				.body("bookingdates.checkin", Matchers.equalTo("2026-02-01"));
	
	
	}	
}
