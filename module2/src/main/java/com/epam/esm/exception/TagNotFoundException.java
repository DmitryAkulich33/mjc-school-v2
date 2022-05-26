package com.epam.esm.exception;

public class TagNotFoundException extends ServiceException {

    public TagNotFoundException(String message) {
        super(message);
    }

    public TagNotFoundException(String message, Object... args) {
        super(message, args);
    }
}
