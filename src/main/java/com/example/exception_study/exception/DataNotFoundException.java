package com.example.exception_study.exception;

import org.springframework.http.HttpStatus;

public class DataNotFoundException extends BusinessException {

    public DataNotFoundException(String resource) {
        super("NOT_FOUND", resource + "을(를) 찾을 수 없습니다", HttpStatus.NOT_FOUND);
    }

    public DataNotFoundException(String resource, Object id) {
        super("NOT_FOUND",
                String.format("%s (ID: %s)을(를) 찾을 수 없습니다", resource, id),
                HttpStatus.NOT_FOUND);
    }
}