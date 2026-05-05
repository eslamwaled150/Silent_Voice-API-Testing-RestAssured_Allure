package tests;

import io.qameta.allure.*;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@Epic("Silent Voice API")
@Feature("Sign Language")
public class SignTest extends BaseTest {

    @Test(priority = 3)
    @Story("Save Sign")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Save a sign sentence — expects 200 OK and signID returned")
    public void testSaveSign() {
        String body = """
            {
              "sentenceEn": "Hello World"
            }
            """;

        signID = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(body)
                .when()
                .post("/api/Sign/save")
                .then()
                .statusCode(200)
                .body("signID", notNullValue())
                .extract().path("signID");

        System.out.println("Sign saved with ID: " + signID);
    }

    @Test(priority = 4)
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

        System.out.println("Sign history retrieved");
    }

    @Test(priority = 5)
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

        System.out.println("Sign by ID retrieved: " + signID);
    }

    @Test(priority = 6)
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

        System.out.println("Non-existing sign correctly rejected");
    }

    @Test(priority = 7)
    @Story("Sign History")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Get sign history without token — expects 401 Unauthorized")
    public void testGetSignHistoryNoToken() {
        given()
                .when()
                .get("/api/Sign/history")
                .then()
                .statusCode(401);

        System.out.println("Unauthorized correctly rejected");
    }
    @Test(priority = 9)
    @Story("Save Sign")
    @Severity(SeverityLevel.NORMAL)
    @Description("Save sign with empty sentence — expects 400 Bad Request")
    public void testSaveSignEmptySentence() {
        String body = """
        {
          "sentenceEn": ""
        }
        """;

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(body)
                .when()
                .post("/api/Sign/save")
                .then()
                .statusCode(400);

        System.out.println("Empty sentence correctly rejected");
    }
    }
