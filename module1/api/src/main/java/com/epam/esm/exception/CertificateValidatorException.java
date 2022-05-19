package com.epam.esm.exception;

public class CertificateValidatorException extends ArgumentsException {

    public CertificateValidatorException(String message) {
        super(message);
    }

    public CertificateValidatorException(String s, Object... args) {
        super(s, args);
    }
}
