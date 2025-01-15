package com.demo.exceptionhandler;

public enum SysError implements Error {
    SE01("001", "Error");

    private String code;
    private String message;

    SysError(String number, String error) {
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
