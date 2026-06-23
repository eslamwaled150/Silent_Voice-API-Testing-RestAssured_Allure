# 🤟 Silent Voice API Testing Project

> An automated API testing suite for the **Silent Voice** platform — a system that helps people with hearing and speech disabilities by transcribing voice to text and converting text to sign language.

**API Under Test:** `http://silentvoice.runasp.net`

---

## 👥 Team Members

| Name | Role |
|------|------|
| Islam Waled | QA Engineer |
| Shrouk Elkassas | QA Engineer |

---

## 🛠️ Tech Stack

| Tool | Version | Purpose |
|------|---------|---------|
| Java | 17 | Programming language |
| RestAssured | 5.4.0 | API testing framework |
| TestNG | 7.9.0 | Test execution & management |
| Allure | 2.27.0 | Test reporting |
| Jackson | 2.17.1 | POJO serialization / deserialization |
| Log4j2 | 2.23.1 | Professional logging |
| Apache POI | 5.2.5 | Excel data-driven testing |
| Maven | — | Build & dependency management |
| GitHub Actions | — | CI/CD pipeline |

---

## 📁 Project Structure

    Silent_Voice-API-Testing-RestAssured_Allure/
    ├── .github/
    │   └── workflows/
    │       └── ci.yml                          # GitHub Actions workflow
    └── API_ITI_Silent_Voice_Allure/
        ├── src/
        │   └── test/
        │       ├── java/
        │       │   ├── Pages/
        │       │   │   ├── Auth.java               # POJO model — Auth requests
        │       │   │   ├── Sign.java               # POJO model — Sign requests
        │       │   │   └── Voice.java              # POJO model — Voice requests
        │       │   ├── tests/
        │       │   │   ├── BaseTest.java           # Base config, setup & login
        │       │   │   ├── AuthTest.java           # Authentication test cases
        │       │   │   ├── SignTest.java           # Sign language test cases
        │       │   │   └── VoiceTest.java          # Voice transcription test cases
        │       │   └── utils/
        │       │       ├── DataLoader.java         # JSON test data reader
        │       │       └── ExcelDataLoader.java    # Excel test data reader
        │       └── resources/
        │           ├── data/
        │           │   ├── auth_data.json          # Auth test data
        │           │   ├── sign_data.json          # Sign test data
        │           │   ├── voice_data.json         # Voice test data
        │           │   └── test_data.xlsx          # Excel data-driven test file
        │           ├── koko.wav                    # Sample WAV file for testing
        │           ├── 0703.mp3                    # Sample MP3 file (negative test)
        │           ├── allure.properties           # Allure configuration
        │           └── log4j2.xml                  # Logging configuration
        ├── pom.xml                                 # Maven dependencies
        ├── testng.xml                              # TestNG suite configuration
        └── run_tests.bat                           # Local test runner script

---

## 🏗️ Design Patterns & Architecture

| Pattern | Implementation |
|---------|---------------|
| **POJO + Jackson** | Request bodies modeled as Java objects, auto-serialized to JSON |
| **API Client Layer** | `BaseTest` centralizes `RequestSpecification` setup |
| **Data-Driven Testing** | Test data loaded from JSON files and Excel sheets via `@DataProvider` |
| **Professional Logging** | Log4j2 replaces all `System.out.println` with timestamped log levels |
| **Page Object (API)** | `Pages/` package separates data models from test logic |

---

## 🧪 Test Coverage

### 🔐 Authentication (`AuthTest`) — 10 Test Cases

| # | Test Case | Type | Expected Result |
|---|-----------|------|-----------------|
| 1 | Register with valid data | ✅ Positive | `200` + OTP message |
| 2 | Confirm email with valid OTP | ✅ Positive | `200` + confirmed |
| 3 | Login with valid credentials | ✅ Positive | `200` + JWT token |
| 4 | Forgot password with valid email | ✅ Positive | `200` OK |
| 5 | Register with duplicate email | ❌ Negative | `400` |
| 6 | Register with exceeded fName length | ❌ Negative | `400` |
| 7 | Register with missing required fields | ❌ Negative | `400` |
| 8 | Confirm email with wrong OTP | ❌ Negative | `400` |
| 9 | Login with wrong password | ❌ Negative | `400` or `401` |
| 10 | Login with non-existing email | ❌ Negative | `400` or `404` |

### 🤟 Sign Language (`SignTest`) — 6 Test Cases

| # | Test Case | Type | Expected Result |
|---|-----------|------|-----------------|
| 1 | Save valid sign sentence (parameterized) | ✅ Positive | `200` + signID |
| 2 | Get sign history | ✅ Positive | `200` + array |
| 3 | Get sign by valid ID | ✅ Positive | `200` + correct signID |
| 4 | Save invalid sentences (parameterized) | ❌ Negative | `400` |
| 5 | Get sign by invalid ID | ❌ Negative | `400` or `404` |
| 6 | Get sign history without token | ❌ Negative | `401` |

### 🎙️ Voice Transcription (`VoiceTest`) — 6 Test Cases

| # | Test Case | Type | Expected Result |
|---|-----------|------|-----------------|
| 1 | Transcribe valid WAV file | ✅ Positive | `200` + voiceID |
| 2 | Get voice history | ✅ Positive | `200` + array |
| 3 | Get voice by valid ID | ✅ Positive | `200` + correct voiceID |
| 4 | Transcribe wrong extension (MP3) | ❌ Negative | `400` |
| 5 | Transcribe invalid language (parameterized) | ❌ Negative | `400` |
| 6 | Get audio with invalid filename (parameterized) | ❌ Negative | `400` or `404` |

---

## 📊 Test Results Summary

| Module | Total | ✅ Positive | ❌ Negative |
|--------|-------|------------|------------|
| Auth | 10 | 4 | 6 |
| Sign | 6 | 3 | 3 |
| Voice | 6 | 3 | 3 |
| **Total** | **22** | **10** | **12** |

---

## 🎥 Test Run Recording

▶️ [Watch the full test run on Google Drive](https://drive.google.com/file/d/1tXuGXQmJSmcgYEvYXnQcbptQzD5WMT2l/view?usp=sharing)

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+

### Run All Tests

    cd API_ITI_Silent_Voice_Allure
    mvn clean test

### Generate & View Allure Report

    .allure\allure-2.27.0\bin\allure.bat serve target\allure-results

---

## ⚙️ CI/CD

This project uses **GitHub Actions** to automatically run tests on every push or pull request to `main`, and on a **daily schedule at 3:00 PM Cairo time**.

The Allure report is published to **GitHub Pages** after each run.

🔗 **Live Report:** [https://eslamwaled150.github.io/Silent_Voice-API-Testing-RestAssured_Allure/](https://eslamwaled150.github.io/Silent_Voice-API-Testing-RestAssured_Allure/)
