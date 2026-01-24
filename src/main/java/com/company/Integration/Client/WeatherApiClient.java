package com.company.Integration.Client;

import com.company.Integration.DTO.WeatherResponse;
import com.company.Integration.Exception.ExternalApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@Slf4j
public class WeatherApiClient {

    @Value("${weather.api.key}")
    private String apiKey;

    @Autowired
    private WebClient webClient;

    public WeatherResponse getWeather(String city){

        log.info("calling external WeatherApi for city={}",city);
       return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.openweathermap.org")
                        .path("/data/2.5/weather")
                        .queryParam("q",city)
                        .queryParam("appid",apiKey)
                        .build()
                )
                .retrieve()
                .onStatus(
                        status->status.is4xxClientError()|| status.is5xxServerError(),
                        response-> Mono.error(new ExternalApiException("Weather API  failed"))
                )
                .bodyToMono(WeatherResponse.class)
                .timeout(Duration.ofSeconds(3))
                .retry(2)
                .block();
    }
}
