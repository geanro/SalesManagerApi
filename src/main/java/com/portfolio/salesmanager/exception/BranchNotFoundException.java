package com.portfolio.salesmanager.exception;

public class BranchNotFoundException extends RuntimeException {
    public BranchNotFoundException(String message) {
        super(message);
    }
}
