package com.demo.exceptionhandler;

import com.demo.exceptionhandler.error.CommonError;
import com.demo.exceptionhandler.error.Error;
import com.demo.exceptionhandler.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.info("handleIllegalArgumentException");
        return new ResponseEntity<>(ErrorResponse.of(CommonError.INVALID_INPUT_VALUE), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.info("handleMethodArgumentNotValidException");
        return new ResponseEntity<>(ErrorResponse.of(CommonError.INVALID_INPUT_VALUE, ex.getBindingResult()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        log.info("handleBindException");
        return new ResponseEntity<>(ErrorResponse.of(CommonError.INVALID_INPUT_VALUE, e.getBindingResult()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.info("handleMethodArgumentTypeMismatchException");
        return new ResponseEntity<>(ErrorResponse.of(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.info("handleHttpRequestMethodNotSupportedException");
        return new ResponseEntity<>(ErrorResponse.of(CommonError.METHOD_NOT_ALLOWED), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.info("handleAccessDeniedException");
        return new ResponseEntity<>(ErrorResponse.of(CommonError.HANDLE_ACCESS_DENIED), HttpStatus.valueOf(CommonError.HANDLE_ACCESS_DENIED.getStatus()));
    }

    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<ErrorResponse> handleBaseException(BaseException e) {
        log.info("handleBaseException");
        Error error = e.getError();
        return new ResponseEntity<>(ErrorResponse.of(error), HttpStatus.valueOf(error.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.info("handleException");
        return new ResponseEntity<>(ErrorResponse.of(CommonError.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
