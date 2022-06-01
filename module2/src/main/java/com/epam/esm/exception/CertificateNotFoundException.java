package com.epam.esm.exception;

public class CertificateNotFoundException extends ServiceException {

    public CertificateNotFoundException(String message, Object... args) {
        super(message, args);
    }
}

