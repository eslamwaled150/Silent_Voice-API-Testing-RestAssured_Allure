package tests;

import io.qameta.allure.*;
import org.testng.annotations.Test;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@Epic("Silent Voice API")
@Feature("Voice Transcription")
public class VoiceTest extends BaseTest {

    // Helper — create fake MP3 bytes in memory (no file needed)
    private File createFakeMP3() {
        try {
            File file = File.createTempFile("fake_audio", ".mp3");
            file.deleteOnExit();
            try (FileOutputStream fos = new FileOutputStream(file)) {
                // MP3 magic bytes
                fos.write(new byte[]{(byte)0xFF, (byte)0xFB, 0x50, 0x00, 0x00, 0x00, 0x00, 0x00});
            }
            return file;
        } catch (IOException e) {
            throw new RuntimeException("Cannot create fake MP3: " + e.getMessage());
        }
    }

    @Test(priority = 13)
    @Story("Transcribe Audio")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Transcribe valid WAV file — expects 200 OK and voiceID")
    public void testTranscribeAudio() {
    // Use ClassLoader to load from resources — works EVERYWHERE
    File audioFile = new File(getClass().getClassLoader().getResource("koko.wav").getFile());

        voiceID = given()
                .header("Authorization", "Bearer " + token)
                .multiPart("audioFile", audioFile, "audio/wav")
                .multiPart("language", "en")
                .when()
                .post("/api/Voice/transcribe")
                .then()
                .statusCode(200)
                .body("message", equalTo("Audio transcribed and translated successfully!"))
                .body("voiceID", notNullValue())
                .extract().path("voiceID");

        System.out.println("Audio transcribed, voiceID: " + voiceID);
    }

    @Test(priority = 14)
    @Story("Voice History")
    @Severity(SeverityLevel.NORMAL)
    @Description("Get voice history — expects 200 OK and non-empty array")
    public void testGetVoiceHistory() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/Voice/history")
                .then()
                .statusCode(200)
                .body("$", not(empty()));

        System.out.println("Voice history retrieved");
    }

    @Test(priority = 15)
    @Story("Get Voice by ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Get voice record by valid ID — expects 200 OK and correct voiceID")
    public void testGetVoiceById() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/Voice/" + voiceID)
                .then()
                .statusCode(200)
                .body("voiceID", equalTo(voiceID))
                .body("transcriptedTextEn", notNullValue());

        System.out.println("Voice by ID retrieved: " + voiceID);
    }

    @Test(priority = 16)
    @Story("Delete Voice")
    @Severity(SeverityLevel.NORMAL)
    @Description("Delete voice record by valid ID — expects 200 OK and success message")
    public void testDeleteVoice() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/api/Voice/" + voiceID)
                .then()
                .statusCode(200)
                .body("message", equalTo("Voice record deleted successfully."));

        System.out.println("Voice deleted: " + voiceID);
    }

    @Test(priority = 17)
    @Story("Transcribe Audio")
    @Severity(SeverityLevel.NORMAL)
    @Description("Transcribe wrong extension (MP3) — expects 400 Bad Request")
    public void testTranscribeWrongExtension() {
        File fakeMP3 = createFakeMP3();

        given()
                .header("Authorization", "Bearer " + token)
                .multiPart("audioFile", fakeMP3, "audio/mpeg")
                .multiPart("language", "en")
                .when()
                .post("/api/Voice/transcribe")
                .then()
                .statusCode(400);

        System.out.println("Wrong extension (MP3) correctly rejected");
    }
}
