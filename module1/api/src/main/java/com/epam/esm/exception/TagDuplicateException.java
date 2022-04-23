package com.epam.esm.exception;

public class TagDuplicateException extends RuntimeException {
    public TagDuplicateException() {
    }

    public TagDuplicateException(String message) {
        super(message);
    }

    public TagDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public TagDuplicateException(Throwable cause) {
        super(cause);
    }
}
