package com.epam.esm.exception;

public class CertificateValidatorException extends RuntimeException {
    public CertificateValidatorException() {
    }

    public CertificateValidatorException(String message) {
        super(message);
    }

    public CertificateValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public CertificateValidatorException(Throwable cause) {
        super(cause);
    }
}
