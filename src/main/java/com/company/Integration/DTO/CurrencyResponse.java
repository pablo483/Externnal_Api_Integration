package com.company.Integration.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class CurrencyResponse implements Serializable {
    private boolean success;
    private String source;
    private String date; // To handle the historical date feature
    // Use @JsonProperty if the JSON says "quotes" but you want to call it "rates"
    @JsonProperty("quotes")
    private Map<String, Double> rates;

}
