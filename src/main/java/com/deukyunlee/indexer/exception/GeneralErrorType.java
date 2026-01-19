package com.deukyunlee.indexer.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 27.
 */
@Getter
public enum GeneralErrorType implements ErrorType {
    GENERAL_RUNTIME_EXCEPTION(100001, HttpStatus.BAD_REQUEST),
    GENERAL_INTERNAL_SERVER_ERROR(100002, HttpStatus.INTERNAL_SERVER_ERROR),
    GENERAL_BAD_REQUEST_EXCEPTION(100003, HttpStatus.BAD_REQUEST),
    GENERAL_VALIDATION_EXCEPTION(100004, HttpStatus.UNPROCESSABLE_ENTITY),
    GENERAL_EXCEPTION(110000, HttpStatus.BAD_REQUEST),
    ;

    private final int code;
    private HttpStatus httpStatus;
    private ErrorLogLevel logLevel;
    private String val;

    GeneralErrorType(int code) {
        this.code = code;
        this.logLevel = ErrorLogLevel.ERROR;
    }

    GeneralErrorType(int code, HttpStatus httpStatus) {
        this.code = code;
        this.logLevel = ErrorLogLevel.ERROR;
        this.httpStatus = httpStatus;
        this.val = "";
    }
}
