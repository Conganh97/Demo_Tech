package com.demo.exceptionhandler.exception;

import com.demo.exceptionhandler.error.Error;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private Error error;

    public BaseException(String message, Error error) {
        super(message);
        this.error = error;
    }

    public BaseException(Error error) {
        this.error = error;
    }
}
