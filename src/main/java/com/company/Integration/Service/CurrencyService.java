package com.company.Integration.Service;

import com.company.Integration.Client.CurrencyApiClient;
import com.company.Integration.DTO.CurrencyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CurrencyService {

    @Autowired
    private CurrencyApiClient currencyApiClient;

    @Cacheable(value = "currency_cache",key = "{#base, #symbols}")
    public CurrencyResponse getRates(String base,String symbols){

        log.info("Service: Fetching rates for {} (filtered by: {})", base, symbols);
        return currencyApiClient.fetchRates(base,symbols);

    }
}
