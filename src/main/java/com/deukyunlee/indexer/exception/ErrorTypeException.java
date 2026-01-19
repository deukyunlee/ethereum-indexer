package com.deukyunlee.indexer.exception;


import lombok.Getter;

import java.io.Serial;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 22.
 */
@Getter
public class ErrorTypeException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1921633241581403949L;

    protected ErrorType errorType;

    public ErrorTypeException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public ErrorTypeException(CustomErrorType errorType) {
        super(errorType.name());
        this.errorType = errorType;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
