package com.portfolio.salesmanager.exception;

public class StockInsufficientException extends RuntimeException {
    public StockInsufficientException(String message) {
        super(message);
    }
}
