# API Documentation

## Base URLs
- **JavaScript Gateway**: `http://localhost:3000`
- **Java Backend**: `http://localhost:8080`

---

## Authentication Endpoints

### 1. User Signup

**Endpoint:** `POST /api/auth/signup`

**Request Body:**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "securePassword123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expiresIn": 86400000,
  "user": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "roles": ["ROLE_USER"]
  }
}
```

---

### 2. User Login

**Endpoint:** `POST /api/auth/login`

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "securePassword123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expiresIn": 86400000,
  "user": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "roles": ["ROLE_USER"]
  }
}
```

---

## Analytics Endpoints

### 3. Create Analytics Event

**Endpoint:** `POST /api/analytics/events`

**Headers:**
```
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "eventType": "page-view",
  "eventName": "homepage",
  "description": "User viewed the homepage",
  "eventData": {
    "page": "/home",
    "duration": 120,
    "referrer": "google.com"
  },
  "sessionId": "session-abc-123"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "eventType": "page-view",
  "eventName": "homepage",
  "description": "User viewed the homepage",
  "eventData": {
    "page": "/home",
    "duration": 120,
    "referrer": "google.com"
  },
  "ipAddress": "127.0.0.1",
  "userAgent": "Mozilla/5.0...",
  "sessionId": "session-abc-123",
  "createdAt": "2024-03-13T10:30:00"
}
```

---

### 4. Get User Events

**Endpoint:** `GET /api/analytics/users/:userId/events`

**Headers:**
```
Authorization: Bearer <token>
```

**Query Parameters:**
- `page` (optional, default: 0) - Page number
- `size` (optional, default: 20) - Items per page

**Example:**
```
GET /api/analytics/users/1/events?page=0&size=10
```

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "eventType": "page-view",
      "eventName": "homepage",
      "description": "User viewed the homepage",
      "createdAt": "2024-03-13T10:30:00"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    }
  },
  "totalElements": 50,
  "totalPages": 5,
  "last": false,
  "first": true,
  "numberOfElements": 10,
  "size": 10,
  "number": 0
}
```

---

### 5. Get Events by Type

**Endpoint:** `GET /api/analytics/events/type/:eventType`

**Headers:**
```
Authorization: Bearer <token>
```

**Example:**
```
GET /api/analytics/events/type/page-view
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "eventType": "page-view",
    "eventName": "homepage",
    "description": "User viewed the homepage",
    "createdAt": "2024-03-13T10:30:00"
  },
  {
    "id": 2,
    "eventType": "page-view",
    "eventName": "about",
    "description": "User viewed about page",
    "createdAt": "2024-03-13T11:00:00"
  }
]
```

---

### 6. Get Analytics Statistics (Admin Only)

**Endpoint:** `GET /api/analytics/statistics`

**Headers:**
```
Authorization: Bearer <token>
```

**Role Required:** ADMIN

**Response (200 OK):**
```json
[
  ["homepage", 150],
  ["about", 80],
  ["contact", 45],
  ["button-click", 300]
]
```

---

### 7. Get Daily Event Count

**Endpoint:** `GET /api/analytics/daily-count`

**Headers:**
```
Authorization: Bearer <token>
```

**Query Parameters:**
- `date` (optional) - Start date in ISO format (default: 30 days ago)

**Example:**
```
GET /api/analytics/daily-count?date=2024-02-01T00:00:00
```

**Response (200 OK):**
```json
[
  ["2024-03-01", 25],
  ["2024-03-02", 30],
  ["2024-03-03", 45]
]
```

---

## Real-time Endpoints

### 8. Get Active Users

**Endpoint:** `GET /api/realtime/active-users`

**Description:** Returns the current number of active WebSocket connections

**Response (200 OK):**
```json
{
  "activeUsers": 42,
  "timestamp": "2024-03-13T12:00:00.000Z"
}
```

---

## WebSocket Events

### Client → Server Events

#### Connect to Socket.IO
```javascript
const socket = io('http://localhost:3000');
```

#### Join Analytics Room
```javascript
socket.emit('join-analytics', {
  userId: 1,
  eventType: 'page-view'
});
```

#### Track Event
```javascript
socket.emit('track-event', {
  userId: 1,
  eventType: 'page-view',
  eventName: 'homepage',
  data: { page: '/home' }
});
```

#### Subscribe to Dashboard
```javascript
socket.emit('subscribe-dashboard', 'dashboard-123');
```

---

### Server → Client Events

#### Connected
```javascript
socket.on('connected', (data) => {
  console.log('Connected with socket ID:', data.socketId);
});
```

#### Event Acknowledged
```javascript
socket.on('event-acknowledged', (data) => {
  console.log('Event acknowledged:', data.timestamp);
});
```

#### Analytics Event Broadcast
```javascript
socket.on('analytics-event', (event) => {
  console.log('New analytics event:', event);
});
```

#### Event Tracked (Broadcast to room)
```javascript
socket.on('event-tracked', (data) => {
  console.log('Event tracked by another user:', data);
});
```

#### System Metrics
```javascript
socket.on('system-metrics', (metrics) => {
  console.log('Active connections:', metrics.activeConnections);
});
```

---

## Error Responses

### 400 Bad Request
```json
{
  "status": "fail",
  "message": "Validation error message"
}
```

### 401 Unauthorized
```json
{
  "status": "error",
  "message": "Invalid or missing token"
}
```

### 403 Forbidden
```json
{
  "status": "error",
  "message": "Insufficient permissions"
}
```

### 404 Not Found
```json
{
  "status": "error",
  "message": "Resource not found"
}
```

### 500 Internal Server Error
```json
{
  "status": "error",
  "message": "Something went wrong!"
}
```

---

## Rate Limiting

- **Window:** 15 minutes
- **Max Requests:** 100 requests per IP

**Response when limit exceeded (429 Too Many Requests):**
```json
{
  "status": "error",
  "message": "Too many requests from this IP, please try again later."
}
```

---

## Authentication Flow

1. **Signup/Login** → Receive JWT token
2. **Include token in Authorization header** for protected endpoints
3. **Token expires after 24 hours** (use refreshToken to get new token)

**Example Request with Auth:**
```bash
curl -X GET http://localhost:3000/api/analytics/users/1/events \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

## Roles & Permissions

| Endpoint | USER | MODERATOR | ADMIN |
|----------|------|-----------|-------|
| POST /api/auth/* | ✅ | ✅ | ✅ |
| POST /api/analytics/events | ✅ | ✅ | ✅ |
| GET /api/analytics/users/:id/events | ✅ | ✅ | ✅ |
| GET /api/analytics/events/type/* | ✅ | ✅ | ✅ |
| GET /api/analytics/daily-count | ✅ | ✅ | ✅ |
| GET /api/analytics/statistics | ❌ | ❌ | ✅ |

---

For more information, see the main [README.md](../README.md).
