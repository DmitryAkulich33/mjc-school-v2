package com.epam.esm.exception;

public class OrderNotFoundException extends ServiceException {
    public OrderNotFoundException(String message, Object... args) {
        super(message, args);
    }
}
