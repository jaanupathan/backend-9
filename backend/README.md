# Polyglot Analytics Platform

A powerful **polyglot backend system** combining **Java Spring Boot** and **JavaScript/Node.js** to demonstrate expertise in both technologies for building enterprise-grade microservices architectures.

## 🚀 Project Overview

This platform showcases:
- **Java Spring Boot**: Robust, type-safe backend with JPA, Security, and RESTful APIs
- **JavaScript/Node.js**: Flexible API Gateway with real-time WebSocket capabilities
- **Microservices Architecture**: Two services working together seamlessly
- **Security**: JWT authentication, role-based access control (RBAC)
- **Real-time Analytics**: WebSocket-powered live event tracking and dashboards

## 🏗️ Architecture

```
┌─────────────────┐
│   Frontend      │
│   (React/Angular)│
└────────┬────────┘
         │
         ▼
┌─────────────────────────┐
│  JavaScript Gateway     │
│  - API Gateway          │
│  - WebSocket Server     │
│  - Real-time Events     │
│  Port: 3000             │
└────────┬────────────────┘
         │ Proxy/JWT
         ▼
┌─────────────────────────┐
│  Java Spring Boot       │
│  - Core Business Logic  │
│  - JPA/Hibernate        │
│  - Database Layer       │
│  - Security & Auth      │
│  Port: 8080             │
└────────┬────────────────┘
         │
         ▼
┌─────────────────────────┐
│  PostgreSQL Database    │
│  Port: 5432             │
└─────────────────────────┘
```

## 📁 Project Structure

```
backend/
├── java-backend/               # Java Spring Boot Service
│   ├── src/
│   │   ├── main/java/com/polyglot/analytics/
│   │   │   ├── config/         # Security & App configuration
│   │   │   ├── controller/     # REST controllers
│   │   │   ├── dto/            # Data Transfer Objects
│   │   │   ├── model/          # JPA Entities
│   │   │   ├── repository/     # Data repositories
│   │   │   ├── security/       # JWT & Security filters
│   │   │   └── service/        # Business logic
│   │   └── resources/
│   ├── pom.xml
│   └── README.md
│
└── javascript-gateway/         # Node.js API Gateway
    ├── src/
    │   ├── middleware/         # Express middleware
    │   ├── routes/             # API routes
    │   ├── utils/              # Utilities (logger, etc.)
    │   └── websocket/          # WebSocket handlers
    ├── package.json
    └── README.md
```

## 🛠️ Technology Stack

### Java Backend
- **Spring Boot 3.2** - Framework
- **Spring Security** - Authentication & Authorization
- **Spring Data JPA** - ORM
- **PostgreSQL** - Primary Database
- **H2** - Development Database
- **JWT** - Token Authentication
- **Lombok** - Boilerplate reduction
- **Maven** - Dependency management

### JavaScript Gateway
- **Express.js** - Web framework
- **Socket.IO** - WebSocket server
- **JWT** - Token validation
- **Axios** - HTTP client
- **Winston** - Logging
- **Helmet** - Security headers
- **Rate Limiter** - API throttling

## 🚀 Getting Started

### Prerequisites

- **Java 17+**
- **Node.js 18+**
- **PostgreSQL 14+**
- **Maven 3.6+**

### 1. Clone the Repository

```bash
cd backend
```

### 2. Setup PostgreSQL Database

```sql
CREATE DATABASE polyglot_analytics;
CREATE USER postgres WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE polyglot_analytics TO postgres;
```

### 3. Configure Java Backend

Edit `java-backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/polyglot_analytics
spring.datasource.username=postgres
spring.datasource.password=postgres
jwt.secret=YourSecretKeyForJWTTokenGenerationMustBeLongEnoughAndSecure2024
```

### 4. Start Java Backend

```bash
cd java-backend
mvn clean install
mvn spring-boot:run
```

Java backend will start on **http://localhost:8080**

### 5. Configure JavaScript Gateway

Edit `javascript-gateway/.env`:

```env
PORT=3000
JAVA_BACKEND_URL=http://localhost:8080
JWT_SECRET=YourSecretKeyForJWTTokenGenerationMustBeLongEnoughAndSecure2024
```

### 6. Start JavaScript Gateway

```bash
cd javascript-gateway
npm install
npm run dev
```

JavaScript gateway will start on **http://localhost:3000**

## 📡 API Endpoints

### Authentication (via JavaScript Gateway)

#### POST `/api/auth/signup`
```json
{
  "username": "john",
  "email": "john@example.com",
  "password": "password123"
}
```

#### POST `/api/auth/login`
```json
{
  "username": "john",
  "password": "password123"
}
```

### Analytics Endpoints

#### POST `/api/analytics/events` (Authenticated)
Track analytics events in real-time.

#### GET `/api/analytics/users/:userId/events` (Authenticated)
Retrieve user-specific events with pagination.

#### GET `/api/analytics/statistics` (Admin only)
Get aggregated analytics statistics.

#### GET `/api/analytics/daily-count` (Authenticated)
Get daily event counts for the last 30 days.

### Real-time Features

#### GET `/api/realtime/active-users`
Get current number of active WebSocket connections.

## 🔌 WebSocket Integration

Connect to the WebSocket server for real-time updates:

```javascript
const socket = io('http://localhost:3000');

// Join analytics room
socket.emit('join-analytics', { userId: 1, eventType: 'page-view' });

// Track event in real-time
socket.on('track-event', (data) => {
  console.log('Event tracked:', data);
});

// Receive live updates
socket.on('analytics-event', (event) => {
  console.log('New event:', event);
});
```

## 🔐 Security Features

- **JWT Authentication**: Stateless token-based auth
- **Role-Based Access Control**: USER, ADMIN, MODERATOR roles
- **Password Encryption**: BCrypt hashing
- **CORS Protection**: Configured origins
- **Rate Limiting**: Prevents abuse
- **Helmet.js**: Security headers
- **Input Validation**: Server-side validation

## 🧪 Testing

### Java Backend Tests

```bash
cd java-backend
mvn test
```

### JavaScript Gateway Tests

```bash
cd javascript-gateway
npm test
```

## 📊 Use Cases

This platform demonstrates:
1. **Polyglot Architecture**: Best practices for multi-language systems
2. **Microservices Communication**: API Gateway pattern
3. **Real-time Data Processing**: WebSocket integration
4. **Enterprise Security**: Production-ready authentication
5. **Scalable Design**: Horizontal scaling capabilities

## 🎯 Key Features

✅ **Dual Technology Stack**: Java + JavaScript expertise  
✅ **RESTful APIs**: Well-documented endpoints  
✅ **Real-time Analytics**: Live event tracking  
✅ **WebSocket Support**: Socket.IO integration  
✅ **JWT Authentication**: Secure token management  
✅ **Role-based Authorization**: Fine-grained access control  
✅ **Database Persistence**: PostgreSQL with JPA  
✅ **Logging & Monitoring**: Winston + Spring Actuator  
✅ **Error Handling**: Centralized error management  
✅ **API Gateway**: Unified entry point  

## 📝 Example Usage

### Register a User

```bash
curl -X POST http://localhost:3000/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "demo",
    "email": "demo@example.com",
    "password": "demo123"
  }'
```

### Login

```bash
curl -X POST http://localhost:3000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "demo",
    "password": "demo123"
  }'
```

### Track Event

```bash
curl -X POST http://localhost:3000/api/analytics/events \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "eventType": "page-view",
    "eventName": "homepage",
    "description": "User viewed homepage"
  }'
```

## 🤝 Contributing

This is a demonstration project. Feel free to fork and customize!

## 📄 License

MIT License

## 👨‍💻 Perfect For

- Showcasing full-stack polyglot expertise
- MAANG company applications
- Senior developer portfolios
- Microservices architecture examples
- Real-time analytics platforms

---

**Built with ❤️ using Java Spring Boot & JavaScript/Node.js**
