package com.epam.esm.exception;

public class OrderDaoException extends ServiceException {

    public OrderDaoException(String message, Object... args) {
        super(message, args);
    }
}
