package com.polyglot.analytics;

import com.polyglot.analytics.model.AnalyticsEvent;
import com.polyglot.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class RepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AnalyticsEventRepository analyticsEventRepository;

    @Test
    public void testSaveAndRetrieveEvent() {
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("page", "/home");
        eventData.put("duration", 120);

        AnalyticsEvent event = AnalyticsEvent.builder()
                .eventType("page-view")
                .eventName("homepage")
                .description("User viewed homepage")
                .eventData(eventData)
                .ipAddress("127.0.0.1")
                .sessionId("session-123")
                .build();

        AnalyticsEvent savedEvent = entityManager.persistAndFlush(event);

        assertThat(savedEvent.getId()).isNotNull();
        assertThat(savedEvent.getEventType()).isEqualTo("page-view");
        assertThat(savedEvent.getEventName()).isEqualTo("homepage");
        assertThat(savedEvent.getCreatedAt()).isNotNull();
    }

    @Test
    public void testFindByEventType() {
        AnalyticsEvent event1 = AnalyticsEvent.builder()
                .eventType("click")
                .eventName("button-click")
                .description("Button clicked")
                .build();

        AnalyticsEvent event2 = AnalyticsEvent.builder()
                .eventType("click")
                .eventName("link-click")
                .description("Link clicked")
                .build();

        entityManager.persistAndFlush(event1);
        entityManager.persistAndFlush(event2);

        List<AnalyticsEvent> events = analyticsEventRepository.findByEventType("click");

        assertThat(events).hasSize(2);
        assertThat(events.stream().allMatch(e -> e.getEventType().equals("click"))).isTrue();
    }

    @Test
    public void testEventStatistics() {
        AnalyticsEvent event1 = AnalyticsEvent.builder()
                .eventType("page-view")
                .eventName("homepage")
                .build();

        AnalyticsEvent event2 = AnalyticsEvent.builder()
                .eventType("page-view")
                .eventName("about")
                .build();

        AnalyticsEvent event3 = AnalyticsEvent.builder()
                .eventType("click")
                .eventName("button")
                .build();

        entityManager.persistAndFlush(event1);
        entityManager.persistAndFlush(event2);
        entityManager.persistAndFlush(event3);

        List<Object[]> stats = analyticsEventRepository.getEventStatistics();

        assertThat(stats).hasSize(2); // 2 unique event names
    }
}
