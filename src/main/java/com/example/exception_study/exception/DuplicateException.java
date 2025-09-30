package com.example.exception_study.exception;

import org.springframework.http.HttpStatus;

public class DuplicateException extends BusinessException {

    public DuplicateException(String resource) {
        super("DUPLICATE_ERROR", "이미 존재하는 " + resource + "입니다", HttpStatus.CONFLICT);
    }

    public DuplicateException(String resource, String value) {
        super("DUPLICATE_ERROR",
                String.format("이미 존재하는 %s입니다: %s", resource, value),
                HttpStatus.CONFLICT);
    }
}