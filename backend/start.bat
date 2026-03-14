@echo off
echo 🚀 Starting Polyglot Analytics Platform...

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java is not installed. Please install Java 17+ first.
    exit /b 1
)

REM Check if Node.js is installed
node --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Node.js is not installed. Please install Node.js 18+ first.
    exit /b 1
)

REM Start Java Backend
echo ☕ Starting Java Spring Boot Backend...
cd java-backend

REM Build and run Java backend
call mvn clean package -DskipTests
start "Java Backend" cmd /c "java -jar target/analytics-platform-1.0.0.jar"

echo ✅ Java Backend started on http://localhost:8080

REM Wait for Java to start
echo ⏳ Waiting for Java backend to initialize...
timeout /t 15 /nobreak >nul

REM Start JavaScript Gateway
cd ..\javascript-gateway

REM Install dependencies if node_modules doesn't exist
if not exist "node_modules" (
    echo 📦 Installing Node.js dependencies...
    call npm install
)

echo 🟢 Starting JavaScript Gateway...
start "JavaScript Gateway" cmd /c "npm start"

echo ✅ JavaScript Gateway started on http://localhost:3000

echo.
echo =========================================
echo 🎉 Platform Started Successfully!
echo =========================================
echo Java Backend:   http://localhost:8080
echo JavaScript GW:  http://localhost:3000
echo H2 Console:     http://localhost:8080/h2-console
echo Health Check:   http://localhost:3000/health
echo.
echo Close both terminal windows to stop the services
echo.

pause
