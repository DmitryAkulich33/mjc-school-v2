package com.epam.esm.exception;

public abstract class ArgumentsException extends RuntimeException {
    private final Object[] args;

    public ArgumentsException(String s, Object... args) {
        super(s);
        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }
}
