package com.company.Integration.Kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaAuditEventConsumer {

    @KafkaListener(topics = "audit-events",groupId = "audit-group")
    public void consume(AuditEvent auditEvent) {
        log.info("Received audit event: {}", auditEvent);
    }
}
