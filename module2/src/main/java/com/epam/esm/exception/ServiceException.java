package com.epam.esm.exception;

public abstract class ServiceException extends RuntimeException {
    private final Object[] args;

    public ServiceException(String message, Object... args) {
        super(message);
        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }
}

