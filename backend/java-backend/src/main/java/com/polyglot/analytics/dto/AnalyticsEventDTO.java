package com.polyglot.analytics.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsEventDTO {
    private Long id;
    private String eventType;
    private String eventName;
    private String description;
    private Map<String, Object> eventData;
    private String ipAddress;
    private String userAgent;
    private String sessionId;
    private LocalDateTime createdAt;
}
