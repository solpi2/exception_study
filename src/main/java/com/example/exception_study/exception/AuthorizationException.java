package com.example.exception_study.exception;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends BusinessException {

    public AuthorizationException(String message) {
        super("AUTHORIZATION_ERROR", message, HttpStatus.FORBIDDEN);
    }

    public static AuthorizationException notOwner() {
        return new AuthorizationException("해당 리소스에 대한 권한이 없습니다");
    }
}