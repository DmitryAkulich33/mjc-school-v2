package com.epam.esm.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    private static Logger log = LogManager.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<Object> handleTagNotFoundException(TagNotFoundException exception) {
        String errorCode = String.format("%s%s", HttpStatus.NOT_FOUND.value(), ErrorCode.TAG_DAO_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CertificateNotFoundException.class)
    public ResponseEntity<Object> handleCertificateNotFoundException(CertificateNotFoundException exception) {
        String errorCode = String.format("%s%s", HttpStatus.NOT_FOUND.value(), ErrorCode.CERTIFICATE_DAO_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TagDaoException.class)
    public ResponseEntity<Object> handleTagDaoException(TagDaoException exception) {
        String errorCode = String.format("%s%s", HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.TAG_DAO_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CertificateDaoException.class)
    public ResponseEntity<Object> handleCertificateDaoException(CertificateDaoException exception) {
        String errorCode = String.format("%s%s", HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.CERTIFICATE_DAO_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TagDuplicateException.class)
    public ResponseEntity<Object> handleTagDuplicateException(TagDuplicateException exception) {
        String errorCode = String.format("%s%s", HttpStatus.BAD_REQUEST.value(), ErrorCode.TAG_DAO_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CertificateDuplicateException.class)
    public ResponseEntity<Object> handleCertificateDuplicateException(CertificateDuplicateException exception) {
        String errorCode = String.format("%s%s", HttpStatus.BAD_REQUEST.value(), ErrorCode.CERTIFICATE_DAO_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> getResponseEntity(Exception exception, String errorCode, HttpStatus httpStatus) {
        String message = exception.getMessage();
        log.error(message, exception);
        ExceptionResponse error = new ExceptionResponse(message, errorCode);
        return new ResponseEntity<>(error, new HttpHeaders(), httpStatus);
    }
}
