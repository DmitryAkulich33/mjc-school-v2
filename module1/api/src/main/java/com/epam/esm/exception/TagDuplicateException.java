package com.epam.esm.exception;

public class TagDuplicateException extends ArgumentsException {

    public TagDuplicateException(String message) {
        super(message);
    }

    public TagDuplicateException(String s, Object... args) {
        super(s, args);
    }
}
