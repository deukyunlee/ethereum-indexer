package com.deukyunlee.indexer.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 22.
 */
public interface ErrorType {
    int getCode();
    HttpStatus getHttpStatus();
    ErrorLogLevel getLogLevel();
}

