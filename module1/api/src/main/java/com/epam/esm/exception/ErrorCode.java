package com.epam.esm.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    TAG_DAO_ERROR_CODE("01"),
    CERTIFICATE_DAO_ERROR_CODE("02"),
    TAG_SERVICE_ERROR_CODE("03"),
    CERTIFICATE_ERROR_CODE("04");

    private final String errorCode;
}
