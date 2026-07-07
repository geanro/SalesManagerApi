package com.portfolio.salesmanager.exception;

public class SaleAlreadyCancelledException extends RuntimeException {
    public SaleAlreadyCancelledException(String message) {
        super(message);
    }
}
