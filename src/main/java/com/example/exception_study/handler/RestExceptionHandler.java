package com.example.exception_study.handler;

import com.example.exception_study.dto.ErrorResponse;
import com.example.exception_study.exception.BusinessException;
import com.example.exception_study.exception.SystemException;
import com.example.exception_study.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class RestExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("Business exception at {}: {}", request.getRequestURI(), e.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(e, request.getRequestURI());

        return ResponseEntity.status(e.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            ValidationException e, HttpServletRequest request) {

        log.warn("Validation exception at {}: {} field errors",
                request.getRequestURI(), e.getFieldErrors().size());

        ErrorResponse errorResponse = ErrorResponse.validation(e, request.getRequestURI());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, HttpServletRequest request) {

        log.warn("Method argument validation failed at {}: {} errors",
                request.getRequestURI(), e.getBindingResult().getFieldErrors().size());

        ValidationException validationException = new ValidationException(
                e.getBindingResult().getFieldErrors()
        );

        ErrorResponse errorResponse = ErrorResponse.validation(validationException, request.getRequestURI());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<ErrorResponse> handleSystemException(
            SystemException e, HttpServletRequest request) {

        log.error("System exception at " + request.getRequestURI(), e);

        ErrorResponse errorResponse = ErrorResponse.of(e, request.getRequestURI());
        return ResponseEntity.status(e.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException e, HttpServletRequest request) {

        log.warn("Illegal argument at {}: {}", request.getRequestURI(), e.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("INVALID_ARGUMENT")
                .message("잘못된 요청입니다: " + e.getMessage())
                .path(request.getRequestURI())
                .timestamp(java.time.LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception e, HttpServletRequest request) {

        log.error("Unexpected exception at " + request.getRequestURI(), e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("INTERNAL_SERVER_ERROR")
                .message("서버 내부 오류가 발생했습니다")
                .path(request.getRequestURI())
                .detail(e.getMessage())
                .timestamp(java.time.LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}