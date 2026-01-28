package com.company.Integration.Kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaAuditEventConsumer {
//simple way of doing
//    @KafkaListener(topics = "audit-events",groupId = "audit-group")
//    public void consume(AuditEvent auditEvent) {
//        log.info("Received audit event: {}", auditEvent);
//    }

    private static final String AUDIT_KEY = "WEATHER_AUDIT_LOG";

    private final RedisTemplate<String, Object> redisTemplate;

    @KafkaListener(topics = "audit_events", groupId = "audit-group", containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload AuditEvent auditEvent, @Header(KafkaHeaders.RECEIVED_PARTITION) int partition, @Header(KafkaHeaders.OFFSET) long offset, Acknowledgment acknowledgment) {

        try {
            log.debug("Consuming event from partition {} offset{}: {}", partition, offset, auditEvent.getEventType());
            processAuditEvent(auditEvent);

            if (acknowledgment != null) {
                acknowledgment.acknowledge();
                log.debug("Successfully acknowledged audit event of offset {}: {}", offset, auditEvent.getEventType());
            }
        } catch (Exception e) {
            log.error("Error processing audit event from partition {} offset {}: {}", partition, offset, auditEvent.getEventType(), e);
            throw e;
        }

    }

    private void processAuditEvent(AuditEvent auditEvent) {
        // 1. Save to Redis List (acting as our database)
        // We store the most recent events at the top
        redisTemplate.opsForList().leftPush(AUDIT_KEY, auditEvent);

        // 2. Keep only the last 50 logs (Optional house-cleaning)
        redisTemplate.opsForList().trim(AUDIT_KEY,0,49);

        log.info("Successfully store event to Redis Storage");

    }
}
