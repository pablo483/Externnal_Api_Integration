package com.company.Integration.Kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaAuditEventProducer {

    private static final String TOPIC = "audit_events";
    private final KafkaTemplate<String, AuditEvent> kafkaTemplate;


    public KafkaAuditEventProducer(KafkaTemplate<String, AuditEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendAuditEvent(AuditEvent auditEvent) {
        log.info("Sending audit event: {}", auditEvent);
        kafkaTemplate.send(TOPIC, auditEvent);
    }
}
