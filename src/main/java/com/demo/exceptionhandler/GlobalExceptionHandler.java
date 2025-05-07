package com.demo.exceptionhandler;

import com.demo.exceptionhandler.error.CommonError;
import com.demo.exceptionhandler.error.Error;
import com.demo.exceptionhandler.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;

/**
 * Global exception handler for the application.
 * Handles various exceptions and returns appropriate error responses.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Creates a ResponseEntity with the given error and HTTP status.
     *
     * @param error The error to include in the response
     * @param status The HTTP status code
     * @return A ResponseEntity containing the error response
     */
    private ResponseEntity<ErrorResponse> createErrorResponse(Error error, HttpStatus status) {
        return new ResponseEntity<>(ErrorResponse.of(error), status);
    }

    /**
     * Creates a ResponseEntity with the given error, binding result, and HTTP status.
     *
     * @param error The error to include in the response
     * @param bindingResult The binding result containing validation errors
     * @param status The HTTP status code
     * @return A ResponseEntity containing the error response
     */
    private ResponseEntity<ErrorResponse> createValidationErrorResponse(
            Error error,
            BindingResult bindingResult,
            HttpStatus status) {
        return new ResponseEntity<>(ErrorResponse.of(error, bindingResult), status);
    }

    /**
     * Handles IllegalArgumentException.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.info("Handling IllegalArgumentException: {}", ex.getMessage());
        return createErrorResponse(CommonError.INVALID_INPUT_VALUE, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles MethodArgumentNotValidException (validation failures).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.info("Handling MethodArgumentNotValidException: {}", ex.getMessage());
        return createValidationErrorResponse(
                CommonError.INVALID_INPUT_VALUE,
                ex.getBindingResult(),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles BindException (form binding failures).
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException ex) {
        log.info("Handling BindException: {}", ex.getMessage());
        return createValidationErrorResponse(
                CommonError.INVALID_INPUT_VALUE,
                ex.getBindingResult(),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles MethodArgumentTypeMismatchException (type conversion failures).
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.info("Handling MethodArgumentTypeMismatchException: {}", ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.of(ex), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles HttpRequestMethodNotSupportedException (unsupported HTTP methods).
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.info("Handling HttpRequestMethodNotSupportedException: {}", ex.getMessage());
        return createErrorResponse(CommonError.METHOD_NOT_ALLOWED, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Handles AccessDeniedException (security access denied).
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.info("Handling AccessDeniedException: {}", ex.getMessage());
        HttpStatus status = HttpStatus.valueOf(CommonError.HANDLE_ACCESS_DENIED.getStatus());
        return createErrorResponse(CommonError.HANDLE_ACCESS_DENIED, status);
    }

    /**
     * Handles BaseException (application-specific exceptions).
     */
    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<ErrorResponse> handleBaseException(BaseException ex) {
        log.info("Handling BaseException: {}", ex.getMessage());
        Error error = ex.getError();
        return createErrorResponse(error, HttpStatus.valueOf(error.getStatus()));
    }

    /**
     * Fallback handler for all other exceptions.
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("Unhandled exception occurred", ex);
        return createErrorResponse(CommonError.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}