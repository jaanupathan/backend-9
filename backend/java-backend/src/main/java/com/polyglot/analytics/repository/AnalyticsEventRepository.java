package com.polyglot.analytics.repository;

import com.polyglot.analytics.model.AnalyticsEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnalyticsEventRepository extends JpaRepository<AnalyticsEvent, Long> {
    
    Page<AnalyticsEvent> findByUserId(Long userId, Pageable pageable);
    
    List<AnalyticsEvent> findByEventType(String eventType);
    
    @Query("SELECT e FROM AnalyticsEvent e WHERE e.user.id = :userId AND e.createdAt BETWEEN :startDate AND :endDate")
    List<AnalyticsEvent> findByUserAndDateRange(
        @Param("userId") Long userId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
    
    @Query("SELECT e.eventName, COUNT(e) FROM AnalyticsEvent e GROUP BY e.eventName ORDER BY COUNT(e) DESC")
    List<Object[]> getEventStatistics();
    
    @Query("SELECT DATE(e.createdAt), COUNT(e) FROM AnalyticsEvent e WHERE e.createdAt >= :date GROUP BY DATE(e.createdAt)")
    List<Object[]> getDailyEventCount(@Param("date") LocalDateTime date);
}
