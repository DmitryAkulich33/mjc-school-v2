package com.epam.esm.exception;

public class CertificateNotFoundException extends ArgumentsException {

    public CertificateNotFoundException(String message) {
        super(message);
    }

    public CertificateNotFoundException(String s, Object... args) {
        super(s, args);
    }
}
