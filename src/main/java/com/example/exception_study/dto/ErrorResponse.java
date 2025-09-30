package com.example.exception_study.dto;

import com.example.exception_study.exception.BaseException;
import com.example.exception_study.exception.ValidationException;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private String code;
    private String message;
    private String detail;
    private String path;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private List<ValidationException.ValidationError> fieldErrors;

    public static ErrorResponse of(String message) {
        return ErrorResponse.builder()
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ErrorResponse of(String code, String message) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ErrorResponse of(BaseException e) {
        return ErrorResponse.builder()
                .code(e.getErrorCode())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ErrorResponse of(BaseException e, String path) {
        return ErrorResponse.builder()
                .code(e.getErrorCode())
                .message(e.getMessage())
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ErrorResponse validation(ValidationException e) {
        return ErrorResponse.builder()
                .code(e.getErrorCode())
                .message(e.getMessage())
                .fieldErrors(e.getFieldErrors())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ErrorResponse validation(ValidationException e, String path) {
        return ErrorResponse.builder()
                .code(e.getErrorCode())
                .message(e.getMessage())
                .path(path)
                .fieldErrors(e.getFieldErrors())
                .timestamp(LocalDateTime.now())
                .build();
    }
}