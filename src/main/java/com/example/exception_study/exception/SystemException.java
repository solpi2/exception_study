package com.example.exception_study.exception;

import org.springframework.http.HttpStatus;

public class SystemException extends BaseException {

    public SystemException(String message) {
        super("SYSTEM_ERROR", message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public SystemException(String message, Throwable cause) {
        super("SYSTEM_ERROR", message, HttpStatus.INTERNAL_SERVER_ERROR, cause);
    }

    public SystemException(String errorCode, String message) {
        super(errorCode, message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}