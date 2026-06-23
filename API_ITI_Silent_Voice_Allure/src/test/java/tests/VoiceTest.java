package tests;

import Pages.Voice;
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

    private File createFakeMP3() {
        try {
            File file = File.createTempFile("fake_audio", ".mp3");
            file.deleteOnExit();
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(new byte[]{(byte)0xFF, (byte)0xFB, 0x50, 0x00, 0x00, 0x00, 0x00, 0x00});
            }
            return file;
        } catch (IOException e) {
            throw new RuntimeException("Cannot create fake MP3: " + e.getMessage());
        }
    }

    @Test(priority = 16)
    @Story("Transcribe Audio - Positive TC")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Transcribe valid WAV file — expects 200 OK and voiceID")
    public void testTranscribeAudio() {
        File audioFile = new File("src/test/resources/koko.wav");
        Voice voiceBody = new Voice("en");

        voiceID = given()
                .header("Authorization", "Bearer " + token)
                .multiPart("audioFile", audioFile, "audio/wav")
                .multiPart("language", voiceBody.getLanguage())
                .when()
                .post("/api/Voice/transcribe")
                .then()
                .statusCode(200)
                .body("message", equalTo("Audio transcribed and translated successfully!"))
                .body("voiceID", notNullValue())
                .extract().path("voiceID");

        log.info("Audio transcribed, voiceID: {}", voiceID);
    }

    @Test(priority = 17)
    @Story("Voice History - Positive TC")
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

        log.info("Voice history retrieved");
    }

    @Test(priority = 18)
    @Story("Get Voice by ID - Positive TC")
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

        log.info("Voice by ID retrieved: {}", voiceID);
    }

    @Test(priority = 19)
    @Story("Delete Voice - Positive TC")
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

        log.info("Voice deleted: {}", voiceID);
    }

    @Test(priority = 20)
    @Story("Transcribe Audio - Negative TC")
    @Severity(SeverityLevel.NORMAL)
    @Description("Transcribe wrong extension (MP3) — expects 400 Bad Request")
    public void testTranscribeWrongExtension() {
        File fakeMP3 = createFakeMP3();
        Voice voiceBody = new Voice("en");

        given()
                .header("Authorization", "Bearer " + token)
                .multiPart("audioFile", fakeMP3, "audio/mpeg")
                .multiPart("language", voiceBody.getLanguage())
                .when()
                .post("/api/Voice/transcribe")
                .then()
                .statusCode(400);

        log.info("Wrong extension (MP3) correctly rejected");
    }

    @Test(priority = 21)
    @Story("Get Audio File - Negative TC")
    @Severity(SeverityLevel.MINOR)
    @Description("Get audio by wrong filename — expects 400 or 404")
    public void testGetAudioWrongFilename() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/Voice/audio/nonexistent_file_xyz.wav")
                .then()
                .statusCode(anyOf(equalTo(400), equalTo(404)));
        log.info("Wrong filename correctly rejected");
    }


    @Test(priority = 22)
    @Story("Transcribe Audio - Negative TC")
    @Severity(SeverityLevel.NORMAL)
    @Description("Transcribe with wrong language type — expects 400 Bad Request")
    public void testTranscribeWrongLanguage() {
        File audioFile = new File("src/test/resources/koko.wav");
        given()
                .header("Authorization", "Bearer " + token)
                .multiPart("audioFile", audioFile, "audio/wav")
                .multiPart("language", "xx_INVALID")
                .when()
                .post("/api/Voice/transcribe")
                .then()
                .statusCode(400)
                .body("message", containsString("Language must be 'en' or 'ar'."));
        log.info("Wrong language type correctly rejected");
    }


}
