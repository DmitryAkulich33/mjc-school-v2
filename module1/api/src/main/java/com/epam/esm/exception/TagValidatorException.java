package com.epam.esm.exception;

public class TagValidatorException extends ArgumentsException {

    public TagValidatorException(String message) {
        super(message);
    }

    public TagValidatorException(String s, Object... args) {
        super(s, args);
    }
}
