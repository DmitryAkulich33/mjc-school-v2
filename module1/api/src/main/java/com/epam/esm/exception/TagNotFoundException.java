package com.epam.esm.exception;

public class TagNotFoundException extends ArgumentsException {

    public TagNotFoundException(String message) {
        super(message);
    }

    public TagNotFoundException(String s, Object... args) {
        super(s, args);
    }
}
