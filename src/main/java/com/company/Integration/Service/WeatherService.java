package com.company.Integration.Service;

import com.company.Integration.Client.WeatherApiClient;
import com.company.Integration.DTO.WeatherResponse;
import com.company.Integration.Kafka.AuditEvent;
import com.company.Integration.Kafka.KafkaAuditEventProducer;
import com.company.Integration.Util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class WeatherService {

    @Autowired
    private WeatherApiClient weatherApiClient;

    @Autowired
    private KafkaAuditEventProducer kafkaAuditEventProducer;

    @Cacheable(value = Constants.WEATHER_CACHE, key = "#city")
    public WeatherResponse getWeather(String city) {
        log.info("Fetching weather for city={}", city);

        WeatherResponse weatherResponse = weatherApiClient.getWeather(city);

        log.info("Weather fetched successfully for city={}", city);

        // ✅ Audit logging (asynchronous - never blocks)
        sendAuditEvent(city, weatherResponse);


        return weatherResponse;
    }

 //✅ PRODUCTION PATTERN: Isolated error handling for side effects
    private void sendAuditEvent(String city, WeatherResponse weatherResponse) {
        try {
            AuditEvent event = new AuditEvent(
                    "WEATEHR_API_CALL",
                    "Weather fetched for city :{}" + city,
                    LocalDateTime.now()
            );

            kafkaAuditEventProducer.sendAuditEvent(event);

        }catch (Exception e){
            log.warn("Failed to publish audit event for city:{}",city, e);

        }
    }
}
