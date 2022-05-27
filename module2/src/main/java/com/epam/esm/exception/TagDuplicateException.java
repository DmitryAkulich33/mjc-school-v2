package com.epam.esm.exception;

public class TagDuplicateException extends ServiceException {
    public TagDuplicateException(String message) {
        super(message);
    }

    public TagDuplicateException(String message, Object... args) {
        super(message, args);
    }
}
