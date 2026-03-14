#!/bin/bash

echo "🚀 Starting Polyglot Analytics Platform..."

# Check if PostgreSQL is running
echo "📊 Checking PostgreSQL connection..."
if ! command -v psql &> /dev/null; then
    echo "⚠️  PostgreSQL not found in PATH. Please ensure it's installed and running."
fi

# Start Java Backend
echo "☕ Starting Java Spring Boot Backend..."
cd java-backend

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven first."
    exit 1
fi

# Build and run Java backend
mvn clean package -DskipTests
java -jar target/analytics-platform-1.0.0.jar &

JAVA_PID=$!
echo "✅ Java Backend started (PID: $JAVA_PID)"

# Wait for Java to start
echo "⏳ Waiting for Java backend to initialize..."
sleep 15

# Start JavaScript Gateway
cd ../javascript-gateway

# Check if Node.js is installed
if ! command -v node &> /dev/null; then
    echo "❌ Node.js is not installed. Please install Node.js first."
    kill $JAVA_PID
    exit 1
fi

# Install dependencies if node_modules doesn't exist
if [ ! -d "node_modules" ]; then
    echo "📦 Installing Node.js dependencies..."
    npm install
fi

echo "🟢 Starting JavaScript Gateway..."
npm start &

JS_PID=$!
echo "✅ JavaScript Gateway started (PID: $JS_PID)"

echo ""
echo "========================================="
echo "🎉 Platform Started Successfully!"
echo "========================================="
echo "Java Backend:   http://localhost:8080"
echo "JavaScript GW:  http://localhost:3000"
echo "H2 Console:     http://localhost:8080/h2-console"
echo "Health Check:   http://localhost:3000/health"
echo ""
echo "Press Ctrl+C to stop all services"
echo ""

# Handle shutdown
trap "echo '🛑 Shutting down...'; kill $JAVA_PID $JS_PID; exit 0" SIGINT SIGTERM

# Wait for processes
wait
