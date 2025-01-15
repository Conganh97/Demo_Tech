package com.demo.exceptionhandler;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private String message;
    private String code;

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
        this.message = message;
    }

    public BaseException(Error error) {
        this.message = error.getMessage();
        this.code = error.getCode();
    }
}
