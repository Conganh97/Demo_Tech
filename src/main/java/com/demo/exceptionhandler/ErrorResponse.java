package com.demo.exceptionhandler;

import org.springframework.beans.factory.annotation.Autowired;
import com.demo.exceptionhandler.error.CommonError;
import com.demo.exceptionhandler.error.Error;
import com.demo.i18n.service.MessageService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ErrorResponse {
    private LocalDateTime timeStamp;
    private String message;
    private String code;
    private int status;
    private List<FieldError> fieldErrors;

    @JsonIgnore
    @Autowired
    private MessageService messageService;

    private ErrorResponse(Error e) {
        this.timeStamp = LocalDateTime.now();
        this.message = e.getMessage();
        this.code = e.getCode();
        this.status = e.getStatus();
        this.fieldErrors = new ArrayList<>();
    }

    private ErrorResponse(Error code, final List<FieldError> errors) {
        this.timeStamp = LocalDateTime.now();
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.fieldErrors = errors;
        this.code = code.getCode();
    }

    public static ErrorResponse of(Error error, BindingResult bindingResult) {
        return new ErrorResponse(error, FieldError.of(bindingResult));
    }

    public static ErrorResponse of(Error error) {
        return new ErrorResponse(error);
    }

    public static ErrorResponse of(MethodArgumentTypeMismatchException e) {
        final String value = e.getValue() != null ? (String) e.getValue() : "";
        final List<FieldError> errors = FieldError.of(e.getName(), value, e.getErrorCode());
        return new ErrorResponse(CommonError.INVALID_INPUT_VALUE, errors);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    private static class FieldError {
        private String field;
        private String value;
        private String reason;

        private FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<FieldError> of(String field, String value, String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }

        public static List<FieldError> of(BindingResult bindingResult) {
            return bindingResult.getFieldErrors().stream()
                    .map(e -> new FieldError(e.getField(),
                            e.getRejectedValue() == null ? "" : e.getRejectedValue().toString(),
                            e.getDefaultMessage()))
                    .toList();
        }
    }
}