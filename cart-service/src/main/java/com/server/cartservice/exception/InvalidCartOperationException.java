package com.server.cartservice.exception;

public class InvalidCartOperationException extends RuntimeException {
    public InvalidCartOperationException(String message) {
        super(message);
    }
}
