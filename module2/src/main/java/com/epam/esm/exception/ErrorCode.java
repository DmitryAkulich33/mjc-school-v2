package com.epam.esm.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    TAG_DAO_ERROR_CODE("01"),
    CERTIFICATE_DAO_ERROR_CODE("02"),
    USER_DAO_ERROR_CODE("03"),
    ORDER_DAO_ERROR_CODE("04"),
    TAG_SERVICE_ERROR_CODE("05"),
    CERTIFICATE_SERVICE_ERROR_CODE("06"),
    USER_SERVICE_ERROR_CODE("07"),
    ORDER_SERVICE_ERROR_CODE("08"),
    VALIDATION_ERROR_CODE("09"),
    SERVER_ERROR_CODE("10"),
    REQUEST_ERROR_CODE("11");

    private final String errorCode;
}
