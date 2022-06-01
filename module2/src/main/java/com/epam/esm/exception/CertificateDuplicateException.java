package com.epam.esm.exception;

public class CertificateDuplicateException extends ServiceException {

    public CertificateDuplicateException(String message, Object... args) {
        super(message, args);
    }
}
