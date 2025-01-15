package com.demo.exceptionhandler;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorDetail {

    private LocalDateTime timeStamp = LocalDateTime.now();
    private String message;
    private String code;

    public ErrorDetail(Error error) {
        message = error.getMessage();
        code = error.getCode();
    }

    public ErrorDetail(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public ErrorDetail(BaseException e) {
        message = e.getMessage();
        code = e.getCode();
    }

    public ErrorDetail(Exception e) {
        message = e.getMessage();
    }
}
