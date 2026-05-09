package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeSuite;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

public class BaseTest {

    public static String token;
    public static int    signID;
    public static int    voiceID;
    public static String registeredEmail;

    protected static final String BASE_URL   = "http://silentvoice.runasp.net";
    protected static final String PASSWORD   = "eslam45/*";
    protected static final String FIXED_OTP  = "123456";

    protected static final Logger log = LogManager.getLogger(BaseTest.class);

    @BeforeSuite
    public void setup() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.filters(new AllureRestAssured());
    }


    protected RequestSpecification jsonRequest() {
        return given()
                .contentType(ContentType.JSON);
    }




}
