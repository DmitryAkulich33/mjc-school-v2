package com.epam.esm.exception;

public class UserDaoException extends ServiceException {
    public UserDaoException(String message) {
        super(message);
    }

    public UserDaoException(String message, Object... args) {
        super(message, args);
    }
}
