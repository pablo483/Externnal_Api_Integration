package com.company.Integration.Client;

import com.company.Integration.Config.WeatherConfig;import com.company.Integration.DTO.WeatherResponse;
import com.company.Integration.Exception.ExternalApiException;
import com.company.Integration.Service.WeatherService;import lombok.extern.slf4j.Slf4j;
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
    private WeatherConfig weatherConfig;

    @Autowired
    private WebClient webClient;

    public WeatherResponse getWeather(String city){

        log.info("calling external WeatherApi for city={}",city);
       return webClient.get()
                .uri( weatherConfig.getBaseUrl(),uriBuilder -> uriBuilder
//                        .scheme("https")
//                        .host("api.openweathermap.org")
                                .path("/weather")                            // .path("/data/2.5/weather")
                        .queryParam("q",city)
                        .queryParam("appid",apiKey)
                        .build()
                )
                .retrieve()
                .onStatus(//Checking if the package delivered is empty or broken.
                        status->status.is4xxClientError()|| status.is5xxServerError(),
                        response-> Mono.error(new ExternalApiException("Weather API  failed"))
                )
                .bodyToMono(WeatherResponse.class)
                .timeout(Duration.ofSeconds(3))
                .retry(2)//Redialing a phone number if the call drops.
                .block();//holds the door open until the JSON is converted into your Java class.
    }
}
