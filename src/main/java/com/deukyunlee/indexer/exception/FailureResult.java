package com.deukyunlee.indexer.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.io.Serial;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 27.
 */
@Getter
public class FailureResult<T> {

    @JsonProperty(required = true)
    private long timestamp;
    @JsonProperty(required = true)
    private int status;
    @JsonProperty(required = true)
    private int errorCode;
    @JsonProperty(required = true)
    private String message;
    @JsonProperty(required = true)
    private T data;

    public FailureResult() {
        this(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public FailureResult(GeneralErrorType generalErrorType, String message) {
        this.errorCode = generalErrorType.getCode();
        this.message = message;
        this.status = generalErrorType.getHttpStatus().value();
        this.timestamp = System.currentTimeMillis();
    }

    public FailureResult(GeneralErrorType generalErrorType, String message, T data) {
        this.errorCode = generalErrorType.getCode();
        this.message = message;
        this.status = generalErrorType.getHttpStatus().value();
        this.timestamp = System.currentTimeMillis();
        this.data = data;
    }

    public FailureResult(ErrorTypeException errorTypeException) {
        this.errorCode = errorTypeException.getErrorType().getCode();
        this.message = errorTypeException.getMessage();
        this.status = errorTypeException.getErrorType().getHttpStatus().value();
        this.timestamp = System.currentTimeMillis();
    }

    public FailureResult(String message, ErrorType errorType) {
        this.errorCode = errorType.getCode();
        this.message = message;
        this.status = errorType.getHttpStatus().value();
        this.timestamp = System.currentTimeMillis();
    }

    public FailureResult(String message, ErrorType errorType, HttpStatus httpStatus) {
        this.errorCode = errorType.getCode();
        this.message = message;
        this.status = httpStatus.value();
        this.timestamp = System.currentTimeMillis();
    }

    public FailureResult(HttpStatus httpStatus) {
        this.errorCode = httpStatus.value();
        this.message = httpStatus.getReasonPhrase();
        this.status = httpStatus.hashCode();
        this.timestamp = System.currentTimeMillis();
    }

    public FailureResult(MissingServletRequestParameterException e) {
        this.errorCode = e.getBody().getStatus();
        this.message = e.getBody().getDetail();
        this.status = e.getBody().getStatus();
        this.timestamp = System.currentTimeMillis();
    }
}

