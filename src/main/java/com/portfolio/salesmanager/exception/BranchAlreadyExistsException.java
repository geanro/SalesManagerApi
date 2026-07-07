package com.portfolio.salesmanager.exception;

public class BranchAlreadyExistsException extends RuntimeException {
    public BranchAlreadyExistsException(String message) {
        super(message);
    }
}
