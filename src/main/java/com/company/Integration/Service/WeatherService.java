package com.company.Integration.Service;

import com.company.Integration.Client.WeatherApiClient;
import com.company.Integration.DTO.WeatherResponse;
import com.company.Integration.Util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WeatherService {

    @Autowired
    private WeatherApiClient weatherApiClient;

    @Cacheable(value = Constants.WEATHER_CACHE,key = "#city")
    public WeatherResponse getWeather(String city){
        log.info("Fetching weather for city={}",city);

        WeatherResponse weatherResponse = weatherApiClient.getWeather(city);

        log.info("Weather fetched successfully for city={}",city);

        return weatherResponse;
    }
}
