package com.example.exception_study.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationException extends BusinessException {
    private final List<ValidationError> fieldErrors;

    public ValidationException(String message) {
        super("VALIDATION_ERROR", message, HttpStatus.BAD_REQUEST);
        this.fieldErrors = new ArrayList<>();
    }

    public ValidationException(List<FieldError> springFieldErrors) {
        super("VALIDATION_ERROR", "입력값 검증에 실패했습니다", HttpStatus.BAD_REQUEST);

        this.fieldErrors = new ArrayList<>();

        if(springFieldErrors != null) {
            for (FieldError fieldError: springFieldErrors) {
                this.fieldErrors.add (new ValidationError(
                        fieldError.getField(),
                        fieldError.getRejectedValue(),
                        fieldError.getDefaultMessage()
                ));
            }
        }
    }

    public ValidationException(String field, Object rejectedValue, String message) {
        super("VALIDATION_ERROR", "입력값 검증에 실패했습니다", HttpStatus.BAD_REQUEST);
        this.fieldErrors = List.of(new ValidationError(field, rejectedValue, message));
    }

    @Getter
    @RequiredArgsConstructor
    public static class ValidationError {
        private final String field;
        private final Object rejectedValue;
        private final String message;
    }
}