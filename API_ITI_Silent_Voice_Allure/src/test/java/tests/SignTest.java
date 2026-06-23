package tests;

import Pages.Sign;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@Epic("Silent Voice API")
@Feature("Sign Language")
public class SignTest extends BaseTest {

    @Test(priority = 10)
    @Story("Save Sign")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Save a sign sentence — expects 200 OK and signID returned")
    public void testSaveSign() {
        Sign signBody = new Sign("Hello World");

        signID = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(signBody)
                .when()
                .post("/api/Sign/save")
                .then()
                .statusCode(200)
                .body("signID", notNullValue())
                .extract().path("signID");

        log.info("Sign saved with ID: {}", signID);
    }

    @Test(priority = 11)
    @Story("Sign History")
    @Severity(SeverityLevel.NORMAL)
    @Description("Get sign history — expects 200 OK and non-empty array")
    public void testGetSignHistory() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/Sign/history")
                .then()
                .statusCode(200)
                .body("$", not(empty()));

        log.info("Sign history retrieved");
    }

    @Test(priority = 12)
    @Story("Get Sign by ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Get sign by valid ID — expects 200 OK and correct signID")
    public void testGetSignById() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/Sign/" + signID)
                .then()
                .statusCode(200)
                .body("signID", equalTo(signID));

        log.info("Sign by ID retrieved: {}", signID);
    }

    @Test(priority = 13)
    @Story("Get Sign by ID")
    @Severity(SeverityLevel.MINOR)
    @Description("Get sign by non-existing ID (99999) — expects 400 or 404")
    public void testGetSignInvalidId() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/Sign/99999")
                .then()
                .statusCode(anyOf(equalTo(404), equalTo(400)));

        log.info("Non-existing sign correctly rejected");
    }

    @Test(priority = 14)
    @Story("Sign History")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Get sign history without token — expects 401 Unauthorized")
    public void testGetSignHistoryNoToken() {
        given()
                .when()
                .get("/api/Sign/history")
                .then()
                .statusCode(401);

        log.info("Unauthorized correctly rejected");
    }

    @Test(priority = 15)
    @Story("Save Sign")
    @Severity(SeverityLevel.NORMAL)
    @Description("Save sign with empty sentence — expects 400 Bad Request")
    public void testSaveSignEmptySentence() {
        Sign signBody = new Sign("");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(signBody)
                .when()
                .post("/api/Sign/save")
                .then()
                .statusCode(400);

        log.info("Empty sentence correctly rejected");
    }


}