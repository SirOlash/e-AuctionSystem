package com.OlashAuctionSystem.exceptions;

public class DenyAccessException extends RuntimeException {
    public DenyAccessException(String message) {
        super(message);
    }
}
