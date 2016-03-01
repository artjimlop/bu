package com.losextraditables.bu;

public class BuApiException extends Exception {

    private final int httpCode;
    private final String marvelCode;

    public BuApiException(int httpCode, String marvelCode, String description, Throwable cause) {
        super(description, cause);
        this.httpCode = httpCode;
        this.marvelCode = marvelCode;
    }

    public BuApiException(String message, Throwable cause) {
        this(-1, "", message, cause);
    }

    public int getHttpCode() {
        return httpCode;
    }

    public String getMarvelCode() {
        return marvelCode;
    }

}
