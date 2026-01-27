package com.company.Integration.Kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AuditEvent {

    private String eventType;
    private String description;
    private LocalDateTime timestamp;
}
