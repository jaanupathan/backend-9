const express = require('express');
const router = express.Router();
const axios = require('axios');
const { authMiddleware, roleMiddleware } = require('../middleware/auth');
const { AppError } = require('../middleware/errorHandler');
const logger = require('../utils/logger');

const JAVA_BACKEND_URL = process.env.JAVA_BACKEND_URL || 'http://localhost:8080';

// Create Axios instance for Java backend
const javaApi = axios.create({
  baseURL: JAVA_BACKEND_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

// Request interceptor to forward auth token
javaApi.interceptors.request.use(
  config => {
    const authHeader = config.headers.Authorization || 
                      (config.originalRequest && config.originalRequest.headers.Authorization);
    if (authHeader) {
      config.headers.Authorization = authHeader;
    }
    return config;
  },
  error => Promise.reject(error)
);

// Response interceptor for error handling
javaApi.interceptors.response.use(
  response => response,
  error => {
    logger.error('Java API Error:', error.response?.data || error.message);
    return Promise.reject(error);
  }
);

/**
 * Proxy route to Java backend authentication
 */
router.post('/auth/login', async (req, res, next) => {
  try {
    const response = await javaApi.post('/api/auth/login', req.body);
    res.json(response.data);
  } catch (error) {
    next(new AppError(
      error.response?.data?.message || 'Login failed',
      error.response?.status || 500
    ));
  }
});

router.post('/auth/signup', async (req, res, next) => {
  try {
    const response = await javaApi.post('/api/auth/signup', req.body);
    res.json(response.data);
  } catch (error) {
    next(new AppError(
      error.response?.data?.message || 'Signup failed',
      error.response?.status || 500
    ));
  }
});

/**
 * Analytics endpoints with caching and real-time broadcasting
 */
router.post('/analytics/events', authMiddleware, async (req, res, next) => {
  try {
    const event = {
      ...req.body,
      ipAddress: req.ip,
      userAgent: req.get('user-agent')
    };

    const response = await javaApi.post('/api/analytics/events', event, {
      headers: { Authorization: `Bearer ${req.headers.authorization.split(' ')[1]}` }
    });

    // Broadcast to WebSocket clients
    if (req.app.get('io')) {
      req.app.get('io').emit('analytics-event', response.data);
    }

    res.json(response.data);
  } catch (error) {
    next(new AppError(
      error.response?.data?.message || 'Failed to create event',
      error.response?.status || 500
    ));
  }
});

router.get('/analytics/users/:userId/events', authMiddleware, async (req, res, next) => {
  try {
    const response = await javaApi.get(`/api/analytics/users/${req.params.userId}/events`, {
      params: req.query,
      headers: { Authorization: `Bearer ${req.headers.authorization.split(' ')[1]}` }
    });
    res.json(response.data);
  } catch (error) {
    next(new AppError(
      error.response?.data?.message || 'Failed to fetch events',
      error.response?.status || 500
    ));
  }
});

router.get('/analytics/statistics', authMiddleware, roleMiddleware('ADMIN'), async (req, res, next) => {
  try {
    const response = await javaApi.get('/api/analytics/statistics', {
      headers: { Authorization: `Bearer ${req.headers.authorization.split(' ')[1]}` }
    });
    res.json(response.data);
  } catch (error) {
    next(new AppError(
      error.response?.data?.message || 'Failed to fetch statistics',
      error.response?.status || 500
    ));
  }
});

router.get('/analytics/daily-count', authMiddleware, async (req, res, next) => {
  try {
    const response = await javaApi.get('/api/analytics/daily-count', {
      params: req.query,
      headers: { Authorization: `Bearer ${req.headers.authorization.split(' ')[1]}` }
    });
    res.json(response.data);
  } catch (error) {
    next(new AppError(
      error.response?.data?.message || 'Failed to fetch daily count',
      error.response?.status || 500
    ));
  }
});

/**
 * JavaScript-native endpoints for real-time features
 */
router.get('/realtime/active-users', (req, res) => {
  const io = req.app.get('io');
  const activeUsers = io.engine.clientsCount;
  res.json({
    activeUsers,
    timestamp: new Date().toISOString()
  });
});

module.exports = router;
