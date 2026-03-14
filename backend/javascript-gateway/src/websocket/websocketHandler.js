const logger = require('../utils/logger');

module.exports = (io) => {
  io.on('connection', (socket) => {
    logger.info(`Client connected: ${socket.id}`);

    // Join a room for real-time analytics updates
    socket.on('join-analytics', (data) => {
      const { userId, eventType } = data;
      
      if (userId) {
        socket.join(`user-${userId}`);
        logger.info(`Socket ${socket.id} joined user-${userId} room`);
      }
      
      if (eventType) {
        socket.join(`event-${eventType}`);
        logger.info(`Socket ${socket.id} joined event-${eventType} room`);
      }
      
      socket.emit('connected', {
        socketId: socket.id,
        timestamp: new Date().toISOString()
      });
    });

    // Handle real-time event tracking
    socket.on('track-event', (data) => {
      logger.info('Event tracked:', data);
      
      // Broadcast to specific rooms
      if (data.userId) {
        socket.to(`user-${data.userId}`).emit('event-tracked', data);
      }
      
      if (data.eventType) {
        socket.to(`event-${data.eventType}`).emit('event-tracked', data);
      }
      
      // Acknowledge receipt
      socket.emit('event-acknowledged', {
        success: true,
        timestamp: new Date().toISOString()
      });
    });

    // Subscribe to live analytics dashboard updates
    socket.on('subscribe-dashboard', (dashboardId) => {
      socket.join(`dashboard-${dashboardId}`);
      logger.info(`Socket ${socket.id} subscribed to dashboard-${dashboardId}`);
      
      socket.emit('subscription-confirmed', {
        dashboardId,
        timestamp: new Date().toISOString()
      });
    });

    // Handle disconnection
    socket.on('disconnect', () => {
      logger.info(`Client disconnected: ${socket.id}`);
    });

    // Error handling
    socket.on('error', (error) => {
      logger.error(`Socket error: ${socket.id}`, error);
    });
  });

  // Periodic broadcast of system metrics
  setInterval(() => {
    io.emit('system-metrics', {
      activeConnections: io.engine.clientsCount,
      timestamp: new Date().toISOString()
    });
  }, 30000); // Every 30 seconds

  logger.info('WebSocket handlers initialized');
};
