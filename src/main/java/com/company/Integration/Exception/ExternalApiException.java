package com.company.Integration.Exception;

public class ExternalApiException extends RuntimeException{

    public ExternalApiException(String message){
        super(message);
    }
}
