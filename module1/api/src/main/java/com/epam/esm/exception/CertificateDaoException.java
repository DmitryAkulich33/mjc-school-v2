package com.epam.esm.exception;

public class CertificateDaoException extends ArgumentsException {

    public CertificateDaoException(String message) {
        super(message);
    }

    public CertificateDaoException(String s, Object... args) {
        super(s, args);
    }
}
