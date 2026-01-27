package com.company.Integration.Controller;

import com.company.Integration.Client.CurrencyApiClient;
import com.company.Integration.DTO.CurrencyResponse;
import com.company.Integration.Service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/currency")
@Slf4j
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/latest")
    public CurrencyResponse getCurrencyRates(@RequestParam(defaultValue = "USD") String base,@RequestParam(required = false) String symbols){
        log.info("Currencies are {}", base);
        return currencyService.getRates(base,symbols);
    }
}
