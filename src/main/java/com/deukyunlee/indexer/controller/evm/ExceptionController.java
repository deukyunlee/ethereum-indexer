package com.deukyunlee.indexer.controller.evm;

import com.deukyunlee.indexer.exception.ErrorType;
import com.deukyunlee.indexer.exception.ErrorTypeException;
import com.deukyunlee.indexer.exception.FailureResult;
import com.deukyunlee.indexer.exception.GeneralErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Map;
import java.util.Optional;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 27.
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<FailureResult<Map<String, Object>>> exception(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        FieldError fieldError = e.getBindingResult().getFieldError();
        Map<String, Object> errorData = Optional.ofNullable(fieldError)
                .map(field -> Map.of(field.getField(), Optional.ofNullable(field.getRejectedValue()).orElse("")))
                .orElse(null);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new FailureResult<>(GeneralErrorType.GENERAL_VALIDATION_EXCEPTION, e.getFieldErrors().get(0).getDefaultMessage(), errorData));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<FailureResult<Void>> exception(RuntimeException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new FailureResult<>(GeneralErrorType.GENERAL_RUNTIME_EXCEPTION, e.getMessage()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<FailureResult<Void>> exception(MissingServletRequestParameterException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new FailureResult<>(e));
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<FailureResult<Void>> exception(HttpServerErrorException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new FailureResult<>(GeneralErrorType.GENERAL_RUNTIME_EXCEPTION, e.getMessage()));
    }

    @ExceptionHandler(ErrorTypeException.class)
    public ResponseEntity<FailureResult<Void>> errorTypeException(ErrorTypeException e) {
        ErrorType errorType = e.getErrorType();

        switch (errorType.getLogLevel()) {
            case INFO -> log.info(e.getMessage(), e);
            case WARN -> log.warn(e.getMessage(), e);
            case ERROR -> log.error(e.getMessage(), e);
            default -> {
            }
        }

        String errorMessage = e.getMessage() + "\nErrorCode : [" + e.getErrorType().getCode() + "]";
        log.error(errorMessage, e);
        return ResponseEntity.status(errorType.getHttpStatus())
                .body(new FailureResult<>(e.getMessage(), e.getErrorType()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<FailureResult<Void>> exception(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new FailureResult<>(GeneralErrorType.GENERAL_EXCEPTION, e.getMessage()));
    }
}
