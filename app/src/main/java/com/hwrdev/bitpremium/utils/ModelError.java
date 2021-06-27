package com.planet.premium.utils;

public class ModelError {
    private String Message;

    public String getMessage() {
        return Message;
    }
    public void setMessage(String Message) {
        this.Message = Message;
    }
    @Override
    public String toString() {
        return "ErrorModel{" +
                "error='" + Message + '\'' +
                '}';
    }
}