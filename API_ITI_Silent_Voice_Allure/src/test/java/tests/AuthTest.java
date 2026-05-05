package tests;

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

        String body = String.format("""
            {
              "fName": "Eslam",
              "lName": "Waled",
              "email": "%s",
              "password": "%s",
              "conPassword": "%s"
            }
            """, registeredEmail, PASSWORD, PASSWORD);

        jsonRequest()
                .body(body)
                .when()
                .post("/api/Auth/register")
                .then()
                .statusCode(200)
                .body("token", nullValue())
                .body("message", containsString("OTP"));

        System.out.println("Registered: " + registeredEmail);
    }

    @Test(priority = 2, dependsOnMethods = "testRegisterValidData")
    @Story("Register")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Confirm email using fixed OTP — expects 200 OK and confirmed message")
    public void testConfirmEmailFixedOtp() {
        String body = String.format("""
            {
              "email": "%s",
              "otp": "%s"
            }
            """, registeredEmail, FIXED_OTP);

        jsonRequest()
                .body(body)
                .when()
                .post("/api/Auth/confirm-email")
                .then()
                .statusCode(200)
                .body("message", containsString("confirmed"));

        System.out.println("Email confirmed with OTP: " + FIXED_OTP);
    }

    @Test(priority = 3, dependsOnMethods = "testConfirmEmailFixedOtp")
    @Story("Login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Login with valid credentials — expects 200 OK and JWT token")
    public void testLoginValidCredentials() {
        String body = String.format("""
            {
              "email": "%s",
              "password": "%s"
            }
            """, registeredEmail, PASSWORD);

        token = jsonRequest()
                .body(body)
                .when()
                .post("/api/Auth/login")
                .then()
                .statusCode(200)
                .body("message", equalTo("Login successful!"))
                .body("token", notNullValue())
                .extract().path("token");

        System.out.println("Token saved: " + token.substring(0, 20) + "...");
    }

    @Test(priority = 4, dependsOnMethods = "testLoginValidCredentials")
    @Story("Login")
    @Severity(SeverityLevel.NORMAL)
    @Description("Login with wrong password — expects 400 or 401")
    public void testLoginWrongPassword() {
        String body = String.format("""
            {
              "email": "%s",
              "password": "WrongPass99"
            }
            """, registeredEmail);

        jsonRequest()
                .body(body)
                .when()
                .post("/api/Auth/login")
                .then()
                .statusCode(anyOf(equalTo(400), equalTo(401)));

        System.out.println("Wrong password correctly rejected");
    }

    @Test(priority = 5, dependsOnMethods = "testRegisterValidData")
    @Story("Register")
    @Severity(SeverityLevel.NORMAL)
    @Description("Register with already-used email — expects 400 Bad Request")
    public void testRegisterDuplicateEmail() {
        String body = String.format("""
            {
              "fName": "Test",
              "lName": "User",
              "email": "%s",
              "password": "%s",
              "conPassword": "%s"
            }
            """, registeredEmail, PASSWORD, PASSWORD);

        jsonRequest()
                .body(body)
                .when()
                .post("/api/Auth/register")
                .then()
                .statusCode(400);

        System.out.println("Duplicate email correctly rejected");
    }

    @Test(priority = 7)
    @Story("Login")
    @Severity(SeverityLevel.NORMAL)
    @Description("Login with non-existing email — expects 400 or 404")
    public void testLoginNonExistingEmail() {
        String body = """
        {
          "email": "nonexisting_user@fake.com",
          "password": "SomePass123"
        }
        """;

        jsonRequest()
                .body(body)
                .when()
                .post("/api/Auth/login")
                .then()
                .statusCode(anyOf(equalTo(400), equalTo(404)));

        System.out.println("Non-existing email correctly rejected");
    }
  }

