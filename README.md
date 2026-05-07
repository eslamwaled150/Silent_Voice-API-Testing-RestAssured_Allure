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
| Maven | — | Build & dependency management |
| GitHub Actions | — | CI/CD pipeline |

---

## 📁 Project Structure

    allure_project/
    ├── src/
    │   └── test/
    │       ├── java/
    │       │   └── tests/
    │       │       ├── BaseTest.java       # Base configuration & setup
    │       │       ├── AuthTest.java       # Authentication test cases
    │       │       ├── SignTest.java       # Sign language test cases
    │       │       └── VoiceTest.java      # Voice transcription test cases
    │       └── resources/
    │           └── koko.wav                # Sample audio file for testing
    ├── .github/
    │   └── workflows/
    │       └── ci.yml                      # GitHub Actions workflow
    ├── pom.xml                             # Maven dependencies
    └── testng.xml                          # TestNG suite configuration

---

## 🧪 Test Coverage

### 🔐 Authentication (`AuthTest`) — 6 Test Cases

| # | Test Case | Type | Expected Result |
|---|-----------|------|-----------------|
| 1 | Register with valid data | ✅ Positive | `200` + OTP message |
| 2 | Confirm email with OTP | ✅ Positive | `200` + confirmed |
| 3 | Login with valid credentials | ✅ Positive | `200` + JWT token |
| 4 | Login with wrong password | ❌ Negative | `400` or `401` |
| 5 | Register with duplicate email | ❌ Negative | `400` |
| 6 | Login with non-existing email | ❌ Negative | `400` or `404` |

### 🤟 Sign Language (`SignTest`) — 6 Test Cases

| # | Test Case | Type | Expected Result |
|---|-----------|------|-----------------|
| 1 | Save a sign sentence | ✅ Positive | `200` + signID |
| 2 | Get sign history | ✅ Positive | `200` + array |
| 3 | Get sign by valid ID | ✅ Positive | `200` + correct signID |
| 4 | Get sign by invalid ID | ❌ Negative | `400` or `404` |
| 5 | Get sign history without token | ❌ Negative | `401` |
| 6 | Save sign with empty sentence | ❌ Negative | `400` |

### 🎙️ Voice Transcription (`VoiceTest`) — 5 Test Cases

| # | Test Case | Type | Expected Result |
|---|-----------|------|-----------------|
| 1 | Transcribe valid WAV file | ✅ Positive | `200` + voiceID |
| 2 | Get voice history | ✅ Positive | `200` + array |
| 3 | Get voice by valid ID | ✅ Positive | `200` + correct voiceID |
| 4 | Delete voice record | ✅ Positive | `200` + success message |
| 5 | Transcribe wrong extension (MP3) | ❌ Negative | `400` |

---

## 📊 Test Results Summary

| Module | Total | ✅ Positive | ❌ Negative |
|--------|-------|------------|------------|
| Auth | 6 | 3 | 3 |
| Sign | 6 | 3 | 3 |
| Voice | 5 | 4 | 1 |
| **Total** | **17** | **10** | **7** |

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+

### Run All Tests

    cd allure_project
    mvn clean test

### Generate & View Allure Report

    .allure\allure-2.27.0\bin\allure.bat serve target\allure-results

---

## ⚙️ CI/CD

This project uses **GitHub Actions** to automatically run tests on every push or pull request to `main`.

The Allure report is published to **GitHub Pages** after each run.

🔗 **Live Report:** [https://eslamwaled150.github.io/Silent_Voice-API-Testing-Project/](https://eslamwaled150.github.io/Silent_Voice-API-Testing-Project/)
