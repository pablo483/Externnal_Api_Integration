package com.company.Integration.Kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditEvent {

    private String eventType;
    private String description;
    private LocalDateTime timestamp;
}
