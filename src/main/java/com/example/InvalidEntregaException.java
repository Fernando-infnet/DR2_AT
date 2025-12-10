package com.example;

public class InvalidEntregaException extends RuntimeException {
    public InvalidEntregaException(String message) {
        super(message);
    }
}
