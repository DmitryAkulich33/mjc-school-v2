package com.epam.esm;

import com.epam.esm.exception.*;
import com.epam.esm.util.LocaleTranslator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    private static Logger log = LogManager.getLogger(ControllerExceptionHandler.class);
    private final LocaleTranslator localeTranslator;

    @Autowired
    public ControllerExceptionHandler(LocaleTranslator localeTranslator) {
        this.localeTranslator = localeTranslator;
    }

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

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException exception) {
        String errorCode = String.format("%s%s", HttpStatus.NOT_FOUND.value(), ErrorCode.USER_DAO_ERROR_CODE.getErrorCode());
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

    @ExceptionHandler(UserDaoException.class)
    public ResponseEntity<Object> handleUserDaoException(UserDaoException exception) {
        String errorCode = String.format("%s%s", HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.USER_DAO_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TagDuplicateException.class)
    public ResponseEntity<Object> handleTagDuplicateException(TagDuplicateException exception) {
        String errorCode = String.format("%s%s", HttpStatus.BAD_REQUEST.value(), ErrorCode.TAG_DAO_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaginationException.class)
    public ResponseEntity<Object> handlePaginationException(PaginationException exception) {
        String errorCode = String.format("%s%s", HttpStatus.BAD_REQUEST.value(), ErrorCode.TAG_DAO_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CertificateDuplicateException.class)
    public ResponseEntity<Object> handleCertificateDuplicateException(CertificateDuplicateException exception) {
        String errorCode = String.format("%s%s", HttpStatus.BAD_REQUEST.value(), ErrorCode.CERTIFICATE_DAO_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception) {
        String errorCode = String.format("%s%s", HttpStatus.BAD_REQUEST.value(), ErrorCode.VALIDATION_ERROR_CODE.getErrorCode());
        return getResponseEntityWithCommonMessage(exception, errorCode, HttpStatus.BAD_REQUEST, "data.not.valid");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception exception) {
        String errorCode = String.format("%s%s", HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.SERVER_ERROR_CODE.getErrorCode());
        return getResponseEntityWithCommonMessage(exception, errorCode, HttpStatus.INTERNAL_SERVER_ERROR, "server.error");
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        String errorCode = String.format("%s%s", HttpStatus.BAD_REQUEST.value(), ErrorCode.VALIDATION_ERROR_CODE.getErrorCode());
        return getResponseEntityWithCommonMessage(exception, errorCode, HttpStatus.BAD_REQUEST, "data.not.valid");
    }


    private ResponseEntity<Object> getResponseEntity(ServiceException exception, String errorCode, HttpStatus httpStatus) {
        String message = localeTranslator.getString(exception.getMessage(), exception.getArgs());
        log.error(message, exception);
        ExceptionResponse error = new ExceptionResponse(message, errorCode);
        return new ResponseEntity<>(error, new HttpHeaders(), httpStatus);
    }

    private ResponseEntity<Object> getResponseEntityWithCommonMessage(Throwable exception, String errorCode, HttpStatus httpStatus,
                                                                      String commonMessage) {
        String message = localeTranslator.getString(commonMessage);
        log.error(message, exception);
        ExceptionResponse error = new ExceptionResponse(message, errorCode);
        return new ResponseEntity<>(error, new HttpHeaders(), httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorCode = String.format("%s%s", HttpStatus.METHOD_NOT_ALLOWED.value(), ErrorCode.REQUEST_ERROR_CODE.getErrorCode());
        return getResponseEntityWithCommonMessage(ex, errorCode, HttpStatus.METHOD_NOT_ALLOWED, "method.not.allowed");
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers,
                                                                     HttpStatus status, WebRequest request) {
        String errorCode = String.format("%s%s", HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), ErrorCode.REQUEST_ERROR_CODE.getErrorCode());
        return getResponseEntityWithCommonMessage(ex, errorCode, HttpStatus.UNSUPPORTED_MEDIA_TYPE, "media.not.supported");

    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorCode = String.format("%s%s", HttpStatus.NOT_ACCEPTABLE.value(), ErrorCode.REQUEST_ERROR_CODE.getErrorCode());
        return getResponseEntityWithCommonMessage(ex, errorCode, HttpStatus.NOT_ACCEPTABLE, "media.not.acceptable");
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorCode = String.format("%s%s", HttpStatus.BAD_REQUEST.value(), ErrorCode.REQUEST_ERROR_CODE.getErrorCode());
        return getResponseEntityWithCommonMessage(ex, errorCode, HttpStatus.BAD_REQUEST, "missing.path.variable");
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorCode = String.format("%s%s", HttpStatus.BAD_REQUEST.value(), ErrorCode.REQUEST_ERROR_CODE.getErrorCode());
        return getResponseEntityWithCommonMessage(ex, errorCode, HttpStatus.BAD_REQUEST, "missing.servlet.request.param");
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorCode = String.format("%s%s", HttpStatus.BAD_REQUEST.value(), ErrorCode.REQUEST_ERROR_CODE.getErrorCode());
        return getResponseEntityWithCommonMessage(ex, errorCode, HttpStatus.BAD_REQUEST, "type.mismatch");
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorCode = String.format("%s%s", HttpStatus.BAD_REQUEST.value(), ErrorCode.REQUEST_ERROR_CODE.getErrorCode());
        return getResponseEntityWithCommonMessage(ex, errorCode, HttpStatus.BAD_REQUEST, "not.readable");
    }
}
