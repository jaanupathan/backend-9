const request = require('supertest');
const { app, server } = require('../src/index');

describe('JavaScript Gateway Tests', () => {
  afterAll((done) => {
    server.close(done);
  });

  describe('Health Check', () => {
    it('should return health status', async () => {
      const response = await request(app)
        .get('/health')
        .expect(200);

      expect(response.body.status).toBe('OK');
      expect(response.body.service).toBe('javascript-gateway');
    });
  });

  describe('Authentication Endpoints', () => {
    let authToken;

    it('should signup a new user', async () => {
      const userData = {
        username: 'testuser',
        email: 'test@example.com',
        password: 'password123'
      };

      const response = await request(app)
        .post('/api/auth/signup')
        .send(userData)
        .expect(200);

      expect(response.body.token).toBeDefined();
      expect(response.body.user.username).toBe('testuser');
      authToken = response.body.token;
    });

    it('should login an existing user', async () => {
      const credentials = {
        username: 'testuser',
        password: 'password123'
      };

      const response = await request(app)
        .post('/api/auth/login')
        .send(credentials)
        .expect(200);

      expect(response.body.token).toBeDefined();
      expect(response.body.user.username).toBe('testuser');
    });
  });

  describe('Protected Routes', () => {
    let validToken;

    beforeAll(async () => {
      // Create a user and get token
      const userData = {
        username: 'protecteduser',
        email: 'protected@example.com',
        password: 'password123'
      };

      const response = await request(app)
        .post('/api/auth/signup')
        .send(userData);

      validToken = response.body.token;
    });

    it('should access analytics events with valid token', async () => {
      const eventData = {
        eventType: 'page-view',
        eventName: 'test-page',
        description: 'Test event'
      };

      const response = await request(app)
        .post('/api/analytics/events')
        .set('Authorization', `Bearer ${validToken}`)
        .send(eventData)
        .expect(200);

      expect(response.body.eventName).toBe('test-page');
    });

    it('should reject access without token', async () => {
      await request(app)
        .post('/api/analytics/events')
        .send({ eventType: 'test' })
        .expect(401);
    });

    it('should reject access with invalid token', async () => {
      await request(app)
        .post('/api/analytics/events')
        .set('Authorization', 'Bearer invalid-token')
        .send({ eventType: 'test' })
        .expect(401);
    });
  });

  describe('Rate Limiting', () => {
    it('should handle multiple requests within limit', async () => {
      for (let i = 0; i < 5; i++) {
        await request(app)
          .get('/health')
          .expect(200);
      }
    });
  });

  describe('Real-time Endpoint', () => {
    it('should return active users count', async () => {
      const response = await request(app)
        .get('/api/realtime/active-users')
        .expect(200);

      expect(response.body.activeUsers).toBeDefined();
      expect(response.body.timestamp).toBeDefined();
    });
  });

  describe('Error Handling', () => {
    it('should handle 404 errors', async () => {
      await request(app)
        .get('/nonexistent-route')
        .expect(404);
    });

    it('should handle validation errors', async () => {
      await request(app)
        .post('/api/auth/login')
        .send({})
        .expect(400);
    });
  });
});
