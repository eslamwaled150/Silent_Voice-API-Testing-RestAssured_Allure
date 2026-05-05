@echo off
cd /d "%~dp0"
title Silent Voice API Tests

echo ==========================================
echo   Silent Voice API - Test Runner
echo ==========================================
echo.

echo [1/2] Running Tests...
call mvn clean test -Dsurefire.useFile=false

echo.
echo [2/2] Opening Allure Report...
.allure\allure-2.27.0\bin\allure.bat serve target\allure-results

pause