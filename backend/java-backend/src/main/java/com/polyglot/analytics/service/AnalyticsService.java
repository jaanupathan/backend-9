package com.polyglot.analytics.service;

import com.polyglot.analytics.dto.AnalyticsEventDTO;
import com.polyglot.analytics.model.AnalyticsEvent;
import com.polyglot.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    
    private final AnalyticsEventRepository analyticsEventRepository;
    
    @Transactional
    public AnalyticsEventDTO createEvent(AnalyticsEvent event) {
        AnalyticsEvent savedEvent = analyticsEventRepository.save(event);
        return mapToDTO(savedEvent);
    }
    
    @Transactional(readOnly = true)
    public Page<AnalyticsEventDTO> getUserEvents(Long userId, Pageable pageable) {
        return analyticsEventRepository.findByUserId(userId, pageable)
                .map(this::mapToDTO);
    }
    
    @Transactional(readOnly = true)
    public List<AnalyticsEventDTO> getEventsByType(String eventType) {
        return analyticsEventRepository.findByEventType(eventType)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<Object[]> getEventStatistics() {
        return analyticsEventRepository.getEventStatistics();
    }
    
    @Transactional(readOnly = true)
    public List<Object[]> getDailyEventCount(LocalDateTime date) {
        return analyticsEventRepository.getDailyEventCount(date);
    }
    
    private AnalyticsEventDTO mapToDTO(AnalyticsEvent event) {
        return AnalyticsEventDTO.builder()
                .id(event.getId())
                .eventType(event.getEventType())
                .eventName(event.getEventName())
                .description(event.getDescription())
                .eventData(event.getEventData())
                .ipAddress(event.getIpAddress())
                .userAgent(event.getUserAgent())
                .sessionId(event.getSessionId())
                .createdAt(event.getCreatedAt())
                .build();
    }
}
