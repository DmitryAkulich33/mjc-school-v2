package com.epam.esm.exception;

public class TagDaoException extends RuntimeException {
    public TagDaoException() {
    }

    public TagDaoException(String message) {
        super(message);
    }

    public TagDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public TagDaoException(Throwable cause) {
        super(cause);
    }
}
