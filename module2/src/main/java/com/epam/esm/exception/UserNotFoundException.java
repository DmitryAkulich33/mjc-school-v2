package com.epam.esm.exception;

public class UserNotFoundException extends ServiceException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Object... args) {
        super(message, args);
    }
}
