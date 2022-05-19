package com.epam.esm.exception;

public class CertificateDuplicateException extends ArgumentsException {

    public CertificateDuplicateException(String message) {
        super(message);
    }

    public CertificateDuplicateException(String s, Object... args) {
        super(s, args);
    }
}
