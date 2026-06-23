# 🤟 Silent Voice API Testing Project

> An automated API testing suite for the **Silent Voice** platform — a system that helps people with hearing and speech disabilities by transcribing voice to text and converting text to sign language.

**API Under Test:** `http://silentvoice.runasp.net`

---

## 👥 Team Members

| Name            | Role        |
| --------------- | ----------- |
| Islam Waled     | QA Engineer |
| Shrouk Elkassas | QA Engineer |

---

## 🛠️ Tech Stack

| Tool           | Version | Purpose                       |
| -------------- | ------- | ----------------------------- |
| Java           | 17      | Programming language          |
| RestAssured    | 5.4.0   | API testing framework         |
| TestNG         | 7.9.0   | Test execution & management   |
| Allure         | 2.27.0  | Test reporting                |
| Maven          | —       | Build & dependency management |
| GitHub Actions | —       | CI/CD pipeline                |
| Postman/Newman | —       | Manual testing & HTML reports |

---

## 📁 Project Structure

```
Silent_Voice-API-Testing-RestAssured_Allure/
├── .github/
│   └── workflows/
│       └── ci.yml                          # GitHub Actions workflow
└── API_ITI_Silent_Voice_Allure/
    ├── src/
    │   └── test/
    │       ├── java/
    │       │   ├── Pages/
    │       │   │   ├── Auth.java           # POJO model — Auth requests
    │       │   │   ├── Sign.java           # POJO model — Sign requests
    │       │   │   └── Voice.java          # POJO model — Voice requests
    │       │   └── tests/
    │       │       ├── BaseTest.java       # Base config, setup & auto-login
    │       │       ├── AuthTest.java       # Authentication test cases
    │       │       ├── SignTest.java       # Sign language test cases
    │       │       └── VoiceTest.java      # Voice transcription test cases
    │       └── resources/
    │           ├── koko.wav                # Sample WAV file (positive tests)
    │           ├── 0703.mp3                # Sample MP3 file (negative tests)
    │           ├── allure.properties       # Allure configuration
    │           └── log4j2.xml              # Logging configuration
    └── pom.xml                             # Maven dependencies
```

---

## 🏗️ Architecture Notes

- **BaseTest** holds shared state: `token`, `signID`, `voiceID`, `registeredEmail`.
- A `@BeforeClass` in `BaseTest` auto-logs in using a fixed pre-registered account (`FIXED_EMAIL`) so that `SignTest` and `VoiceTest` can run independently of `AuthTest`.
- **Token chaining** is handled via TestNG `dependsOnMethods` annotations to ensure correct execution order within `AuthTest`.
- A **fixed OTP** (`FIXED_OTP`) is used in the automated environment to bypass real email delivery and ensure repeatable test execution.

---

## 🧪 Test Coverage

### 🔐 Authentication (`AuthTest`) — 9 Test Cases

| # | Test Case                          | Type        | Expected Result      |
|---|------------------------------------|-------------|----------------------|
| 1 | Register with valid data           | ✅ Positive  | `200` + OTP message  |
| 2 | Confirm email with fixed OTP       | ✅ Positive  | `200` + confirmed    |
| 3 | Login with valid credentials       | ✅ Positive  | `200` + JWT token    |
| 4 | Forgot password with valid email   | ✅ Positive  | `200`                |
| 5 | Login with wrong password          | ❌ Negative  | `400` or `401`       |
| 6 | Register with duplicate email      | ❌ Negative  | `400`                |
| 7 | Login with non-existing email      | ❌ Negative  | `400` or `404`       |
| 8 | Register with exceeded name length | ❌ Negative  | `400`                |
| 9 | Confirm email with wrong OTP       | ❌ Negative  | `400`                |

---

### 🤟 Sign Language (`SignTest`) — 6 Test Cases

| #  | Test Case                      | Type        | Expected Result         |
|----|--------------------------------|-------------|-------------------------|
| 10 | Save a sign sentence           | ✅ Positive  | `200` + signID          |
| 11 | Get sign history               | ✅ Positive  | `200` + non-empty array |
| 12 | Get sign by valid ID           | ✅ Positive  | `200` + correct signID  |
| 13 | Get sign by invalid ID         | ❌ Negative  | `400` or `404`          |
| 14 | Get sign history without token | ❌ Negative  | `401`                   |
| 15 | Save sign with empty sentence  | ❌ Negative  | `400`                   |

---

### 🎙️ Voice Transcription (`VoiceTest`) — 7 Test Cases

| #  | Test Case                           | Type        | Expected Result                    |
|----|-------------------------------------|-------------|------------------------------------|
| 16 | Transcribe valid WAV file           | ✅ Positive  | `200` + voiceID                    |
| 17 | Get voice history                   | ✅ Positive  | `200` + non-empty array            |
| 18 | Get voice by valid ID               | ✅ Positive  | `200` + correct voiceID            |
| 19 | Delete voice record                 | ✅ Positive  | `200` + success message            |
| 20 | Transcribe wrong extension (MP3)    | ❌ Negative  | `400`                              |
| 21 | Get audio by wrong filename         | ❌ Negative  | `400` or `404`                     |
| 22 | Transcribe with wrong language code | ❌ Negative  | `400` + language validation message|

---

## 📊 Test Results Summary

| Module    | Total  | ✅ Positive | ❌ Negative |
| --------- | ------ | ----------- | ----------- |
| Auth      | 9      | 4           | 5           |
| Sign      | 6      | 3           | 3           |
| Voice     | 7      | 4           | 3           |
| **Total** | **22** | **11**      | **11**      |

---

## 🐛 Bugs Discovered During Testing

| Severity   | Endpoint                  | Description                                                                 |
|------------|---------------------------|-----------------------------------------------------------------------------|
| 🔴 CRITICAL | `POST /api/Auth/register` | Returns `400` when Azure DB monthly free tier limit is reached (infrastructure issue, not a code bug) |
| 🟠 HIGH     | `GET /api/Sign/history`   | Intermittently returns `400` due to transient DB failure                    |
| 🟠 HIGH     | `DELETE /api/Sign/{id}`   | `NullReferenceException` thrown — value cannot be null, causes `400` on delete |

---

## 🎥 Demo Recordings

| Type       | Link |
|------------|------|
| Automation | [Watch Automation Demo](https://drive.google.com/file/d/1tXuGXQmJSmcgYEvYXnQcbptQzD5WMT2l/view?usp=sharing) |
| Manual     | [Watch Manual Demo](https://drive.google.com/file/d/102rnqjeWIsmO3sLSOeP_YXiMNVUlvKQU/view) |

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+

### Run All Tests

```bash
cd API_ITI_Silent_Voice_Allure
mvn clean test
```

### Generate & View Allure Report Locally

```bash
.allure\allure-2.27.0\bin\allure.bat serve target\allure-results
```

---

## ⚙️ CI/CD Pipeline

This project uses **GitHub Actions** to automatically run all tests on every push to `main`.

Flow: `Push to GitHub` → `GitHub Actions triggers` → `Maven runs all tests` → `Allure generates report` → `Published to GitHub Pages`

🔗 **Live Allure Report:** https://eslamwaled150.github.io/Silent_Voice-API-Testing-RestAssured_Allure/

🔗 **Live Newman Report:** https://eslamwaled150.github.io/silent-voice-api/newman-report.html
