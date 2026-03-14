package com.polyglot.analytics.controller;

import com.polyglot.analytics.dto.AnalyticsEventDTO;
import com.polyglot.analytics.model.AnalyticsEvent;
import com.polyglot.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AnalyticsController {
    
    private final AnalyticsService analyticsService;
    
    @PostMapping("/events")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    public ResponseEntity<AnalyticsEventDTO> createEvent(@RequestBody AnalyticsEvent event) {
        return ResponseEntity.ok(analyticsService.createEvent(event));
    }
    
    @GetMapping("/users/{userId}/events")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    public ResponseEntity<Page<AnalyticsEventDTO>> getUserEvents(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(analyticsService.getUserEvents(userId, pageable));
    }
    
    @GetMapping("/events/type/{eventType}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    public ResponseEntity<List<AnalyticsEventDTO>> getEventsByType(@PathVariable String eventType) {
        return ResponseEntity.ok(analyticsService.getEventsByType(eventType));
    }
    
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Object[]>> getStatistics() {
        return ResponseEntity.ok(analyticsService.getEventStatistics());
    }
    
    @GetMapping("/daily-count")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    public ResponseEntity<List<Object[]>> getDailyCount(
            @RequestParam(required = false) LocalDateTime date) {
        if (date == null) {
            date = LocalDateTime.now().minusDays(30);
        }
        return ResponseEntity.ok(analyticsService.getDailyEventCount(date));
    }
}
