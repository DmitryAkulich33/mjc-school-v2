package com.epam.esm.exception;

public class CertificateDaoException extends ServiceException {
    public CertificateDaoException(String message) {
        super(message);
    }

    public CertificateDaoException(String message, Object... args) {
        super(message, args);
    }
}
