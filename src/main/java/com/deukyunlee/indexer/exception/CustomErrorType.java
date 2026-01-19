package com.deukyunlee.indexer.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 22.
 */
public enum CustomErrorType implements ErrorType {
    // RPC 3200~3299
    WRONG_RPC_METHOD_TYPE(3200, HttpStatus.INTERNAL_SERVER_ERROR),
    RPC_CALL_FAILED(3201, HttpStatus.INTERNAL_SERVER_ERROR),

    // NUMBER 3300~3399
    CONVERT_HEX_TO_BIG_DECIMAL_ERROR(3300, HttpStatus.INTERNAL_SERVER_ERROR),
    NULL_HEX_STRING_NOT_ALLOWED(3301, HttpStatus.INTERNAL_SERVER_ERROR),
    NULL_BALANCE_NOT_ALLOWED(3302, HttpStatus.INTERNAL_SERVER_ERROR),

    // TOKEN 3400~3499
    TOKEN_NOT_EXISTS_ON_CHAIN(3400, HttpStatus.BAD_REQUEST),

    // BLOCK 3500~3599
    DAILY_LAST_BLOCK_NOT_FOUND(3500, HttpStatus.BAD_REQUEST),
    FIRST_BLOCK_NOT_FOUND(3501, HttpStatus.INTERNAL_SERVER_ERROR),
    LAST_BLOCK_NOT_FOUND(3502, HttpStatus.INTERNAL_SERVER_ERROR),
    BLOCK_NOT_FOUND_FOR_DATE(3503, HttpStatus.INTERNAL_SERVER_ERROR),

    // LOG 3600~3699
    WRONG_TOPIC_VALUE(3600, HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private final int code;
    private final HttpStatus httpStatus;
    private final ErrorLogLevel logLevel;

    CustomErrorType(int code, HttpStatus httpStatus) {
        this.code = code;
        this.logLevel = ErrorLogLevel.ERROR;
        this.httpStatus = httpStatus;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public ErrorLogLevel getLogLevel() {
        return this.logLevel;
    }

}
