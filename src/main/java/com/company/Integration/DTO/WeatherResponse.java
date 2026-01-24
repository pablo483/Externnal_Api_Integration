package com.company.Integration.DTO;

import lombok.Data;

import java.io.Serializable;

@Data
public class WeatherResponse implements Serializable {
    private Main main;

    @Data
    public static class Main implements Serializable{
        private double temp;
        private int humidity;
    }
}
