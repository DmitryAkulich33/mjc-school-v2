package com.epam.esm.exception;

public class TagDaoException extends ServiceException {

    public TagDaoException(String message, Object... args) {
        super(message, args);
    }
}
