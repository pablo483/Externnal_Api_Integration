package com.company.Integration.Config;
import lombok.Data;import org.springframework.boot.context.properties.ConfigurationProperties;import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "webclient.weather")
@Data
public class WeatherConfig {
    private String baseUrl;
    private String timeout;
}
