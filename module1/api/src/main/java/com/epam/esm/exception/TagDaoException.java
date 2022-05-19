package com.epam.esm.exception;

public class TagDaoException extends ArgumentsException {

    public TagDaoException(String message) {
        super(message);
    }

    public TagDaoException(String s, Object... args) {
        super(s, args);
    }
}
