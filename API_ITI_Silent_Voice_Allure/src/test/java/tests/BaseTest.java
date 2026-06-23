package tests;

import Pages.Auth;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
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

    protected static final String FIXED_EMAIL    = "eslam_fixed_test@minitts.net";

    protected final Logger log = LogManager.getLogger(this.getClass());


    @BeforeSuite
    public void setup() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.filters(new AllureRestAssured());
    }


    /** POST JSON without token */
    protected RequestSpecification jsonRequest() {
        return given()
                .contentType(ContentType.JSON);
    }

    @BeforeClass
    public void loginBeforeClass() {
        // Skip if token already set (AuthTest already ran)
        if (token != null && !token.isEmpty()) {
            log.info("Token already exists, skipping login");
            return;
        }

        Auth loginBody = new Auth(FIXED_EMAIL, PASSWORD, true);

        token = given()
                .contentType(ContentType.JSON)
                .body(loginBody)
                .when()
                .post("/api/Auth/login")
                .then()
                .statusCode(200)
                .extract().path("token");

        log.info("Token obtained in BeforeClass: {}...", token.substring(0, 20));
    }


}