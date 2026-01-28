package com.company.Integration.Kafka;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class KafkaAuditEventProducer {

    private static final String TOPIC = "audit_events";
    private final KafkaTemplate<String, AuditEvent> kafkaTemplate;

//we can also use @RequiredArg constructor or @Autowired
    public KafkaAuditEventProducer(KafkaTemplate<String, AuditEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    //Simple way of doing
//    public void sendAuditEvent(AuditEvent auditEvent) {
//        log.info("Sending audit event: {}", auditEvent);
//        kafkaTemplate.send(TOPIC, auditEvent);
//    }


         // ✅ PRODUCTION-GRADE: Async, non-blocking, with error handling

    @Async//runs in separate thread pool
    public CompletableFuture<Void> sendAuditEvent(AuditEvent auditEvent) {

        log.debug("Sending audit event: {}", auditEvent.getEventType());

            return kafkaTemplate.send(TOPIC, auditEvent)
                    .thenAccept(result -> {
                       SendResult<String,AuditEvent> sendResult=result;
                        log.debug("Event published successfully to  partition {} with offset {}",sendResult.getRecordMetadata().partition(),sendResult.getRecordMetadata().offset());
                    }).exceptionally(ex->{
                    log.error("Error sending audit event",auditEvent,ex);
                    return null;
                    });

    }

    /**
     * ✅ Synchronous version for critical events that MUST be logged(for future use not using yet
     */
//    public void publishEventSync(AuditEvent event) {
//        try {
//            SendResult<String, AuditEvent> result = kafkaTemplate.send(TOPIC, event)
//                    .get(5, java.util.concurrent.TimeUnit.SECONDS);  // Wait max 5 seconds
//
//            log.info("Critical event published successfully: {}", event.getEventType());
//
//        } catch (Exception e) {
//            log.error("CRITICAL: Failed to publish mandatory audit event", e);
//            throw new RuntimeException("Audit logging failed", e);
//        }
//    }
//}

}
