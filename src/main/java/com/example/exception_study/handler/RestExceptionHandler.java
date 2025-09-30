package com.example.exception_study.handler;

import com.example.exception_study.dto.ErrorResponse;
import com.example.exception_study.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class RestExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("Business exception at {}: {}", request.getRequestURI(), e.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(e, request.getRequestURI());

        return ResponseEntity.status(e.getHttpStatus()).body(errorResponse);
    }
}