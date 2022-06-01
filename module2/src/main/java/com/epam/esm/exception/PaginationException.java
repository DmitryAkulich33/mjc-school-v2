package com.epam.esm.exception;

public class PaginationException extends ServiceException {

    public PaginationException(String message, Object... args) {
        super(message, args);
    }
}
