package com.company.Integration.Client;

import com.company.Integration.DTO.CurrencyResponse;
import com.company.Integration.Exception.ExternalApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@Slf4j
public class CurrencyApiClient {

    @Autowired
    private WebClient webClient;

    public CurrencyResponse fetchRates(String base,String symbols) {
        log.info("Fetching currency rates from base {}", base);
        return webClient.get()
//                .uri("https://api.exchangerate.host/latest?base={base}",base)
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.apilayer.com")
                        .path("/currency_data/live")
                        .queryParam("source", base)
                        .queryParam("currencies", symbols) // e.g., "EUR,GBP"
                        .queryParam("apikey", "TDcAPLIgKKb4ECah59tMtAwzpiE3BEFT")    // Your new API Key
                        .build()
                )

                .retrieve()
                .onStatus(status -> status.isError(),
                         response-> Mono.error(new ExternalApiException("Currency API failed"))
                )
                .bodyToMono(CurrencyResponse.class)
                .timeout(Duration.ofSeconds(3))
                .retry(2)
                .block();
    }
}
