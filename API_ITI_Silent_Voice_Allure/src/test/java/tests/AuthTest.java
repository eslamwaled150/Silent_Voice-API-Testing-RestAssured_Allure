package tests;

import Pages.Auth;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

@Epic("Silent Voice API")
@Feature("Authentication")
public class AuthTest extends BaseTest {

    @Test(priority = 1)
    @Story("Register")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Register a new user with valid data — expects 200 OK and OTP message")
    public void testRegisterValidData() {
        registeredEmail = "eslam_auto_" + System.currentTimeMillis() + "@minitts.net";

        Auth registerBody = new Auth("Eslam", "Waled", registeredEmail, PASSWORD, PASSWORD);

        jsonRequest()
                .body(registerBody)
                .when()
                .post("/api/Auth/register")
                .then()
                .statusCode(200)
                .body("token", equalTo(""))
                .body("message", containsString("OTP"));

        log.info("Registered: {}", registeredEmail);
    }

    @Test(priority = 2, dependsOnMethods = "testRegisterValidData")
    @Story("Register")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Confirm email using fixed OTP — expects 200 OK and confirmed message")
    public void testConfirmEmailFixedOtp() {
        Auth confirmBody = new Auth(registeredEmail, FIXED_OTP);

        jsonRequest()
                .body(confirmBody)
                .when()
                .post("/api/Auth/confirm-email-FO")
                .then()
                .statusCode(200)
                .body("message", containsString("confirmed"));

        log.info("Email confirmed with OTP: {}", FIXED_OTP);
    }

    @Test(priority = 3, dependsOnMethods = "testConfirmEmailFixedOtp")
    @Story("Login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Login with valid credentials — expects 200 OK and JWT token")
    public void testLoginValidCredentials() {
        Auth loginBody = new Auth(registeredEmail, PASSWORD, true);

        token = jsonRequest()
                .body(loginBody)
                .when()
                .post("/api/Auth/login")
                .then()
                .statusCode(200)
                .body("message", equalTo("Login successful!"))
                .body("token", notNullValue())
                .extract().path("token");

        log.info("Token saved: {}...", token.substring(0, 20));
    }

    @Test(priority = 4, dependsOnMethods = "testLoginValidCredentials")
    @Story("Forgot Password")
    @Severity(SeverityLevel.NORMAL)
    @Description("Forgot password with registered email — expects 200 OK")
    public void testForgotPasswordValidEmail() {
        Auth body = new Auth(registeredEmail, null, false);
        jsonRequest()
                .body("{\"email\":\"" + registeredEmail + "\"}")
                .when()
                .post("/api/Auth/forgot-password")
                .then()
                .statusCode(200);
        log.info("Forgot password OTP sent to: {}", registeredEmail);
    }

    @Test(priority = 5, dependsOnMethods = "testLoginValidCredentials")
    @Story("Login")
    @Severity(SeverityLevel.NORMAL)
    @Description("Login with wrong password — expects 400 or 401")
    public void testLoginWrongPassword() {
        Auth loginBody = new Auth(registeredEmail, "WrongPass99", true);

        jsonRequest()
                .body(loginBody)
                .when()
                .post("/api/Auth/login")
                .then()
                .statusCode(anyOf(equalTo(400), equalTo(401)));

        log.info("Wrong password correctly rejected");
    }

    @Test(priority = 6, dependsOnMethods = "testRegisterValidData")
    @Story("Register")
    @Severity(SeverityLevel.NORMAL)
    @Description("Register with already-used email — expects 400 Bad Request")
    public void testRegisterDuplicateEmail() {
        Auth registerBody = new Auth("Test", "User", registeredEmail, PASSWORD, PASSWORD);

        jsonRequest()
                .body(registerBody)
                .when()
                .post("/api/Auth/register")
                .then()
                .statusCode(400);

        log.info("Duplicate email correctly rejected");
    }

    @Test(priority = 7)
    @Story("Login")
    @Severity(SeverityLevel.NORMAL)
    @Description("Login with non-existing email — expects 400 or 404")
    public void testLoginNonExistingEmail() {
        Auth loginBody = new Auth("nonexisting_user@fake.com", "SomePass123", true);

        jsonRequest()
                .body(loginBody)
                .when()
                .post("/api/Auth/login")
                .then()
                .statusCode(anyOf(equalTo(400), equalTo(404)));

        log.info("Non-existing email correctly rejected");
    }


    @Test(priority = 8)
    @Story("Register")
    @Severity(SeverityLevel.MINOR)
    @Description("Register with exceeded fName length — expects 400 Bad Request")
    public void testRegisterExceededFName() {
        Auth body = new Auth(
                "AhmedSASASASAafdsfdsAhmedSASASASAafdsfdgfhfghfhfghgfsAhmedSASASASA",
                "Ali", registeredEmail, PASSWORD, PASSWORD);
        jsonRequest()
                .body(body)
                .when()
                .post("/api/Auth/register")
                .then()
                .statusCode(400);
        log.info("Exceeded fName correctly rejected");
    }

    @Test(priority = 9, dependsOnMethods = "testRegisterValidData")
    @Story("Register")
    @Severity(SeverityLevel.NORMAL)
    @Description("Confirm email with wrong OTP — expects 400 Bad Request")
    public void testConfirmEmailWrongOtp() {
        Auth body = new Auth(registeredEmail, "000000");
        jsonRequest()
                .body(body)
                .when()
                .post("/api/Auth/confirm-email")
                .then()
                .statusCode(400);
        log.info("Wrong OTP correctly rejected");
    }

  }

