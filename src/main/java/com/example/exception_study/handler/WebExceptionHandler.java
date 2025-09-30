package com.example.exception_study.handler;

import com.example.exception_study.exception.AuthorizationException;
import com.example.exception_study.exception.BusinessException;
import com.example.exception_study.exception.DataNotFoundException;
import com.example.exception_study.exception.SystemException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice(annotations = Controller.class)
public class WebExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public String handleDataNotFound(DataNotFoundException e, Model model, HttpServletRequest request) {
        log.warn("Data not found at {}: {}", request.getRequestURI(), e.getMessage());

        model.addAttribute("errorCode", e.getErrorCode());
        model.addAttribute("errorMessage", e.getMessage());
        model.addAttribute("requestUrl", request.getRequestURL());

        return "error/404";
    }

    @ExceptionHandler(AuthorizationException.class)
    public String handleAuthorization(AuthorizationException e, Model model, HttpServletRequest request) {
        log.warn("Authorization error at {}: {}", request.getRequestURI(), e.getMessage());

        model.addAttribute("errorCode", e.getErrorCode());
        model.addAttribute("errorMessage", e.getMessage());
        model.addAttribute("requestUrl", request.getRequestURI());

        return "error/403";
    }

    @ExceptionHandler(BusinessException.class)
    public String handleBusinessException(BusinessException e, Model model, HttpServletRequest request) {
        log.warn("Business exception at {}: {}", request.getRequestURI(), e.getMessage());

        model.addAttribute("errorCode", e.getErrorCode());
        model.addAttribute("errorMessage", e.getMessage());
        model.addAttribute("requestUrl", request.getRequestURI());
        model.addAttribute("httpStatus", e.getHttpStatus().value());

        return "error/business";
    }

    @ExceptionHandler(SystemException.class)
    public String handleSystemException(SystemException e, Model model, HttpServletRequest request) {
        log.error("System exception at " + request.getRequestURI(), e);

        model.addAttribute("errorCode", e.getErrorCode());
        model.addAttribute("errorMessage", "시스템 오류가 발생했습니다. 관리자에게 문의하세요.");
        model.addAttribute("requestUrl", request.getRequestURI());

        return "error/500";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception e, Model model, HttpServletRequest request) {
        log.error("Unexpected exception at " + request.getRequestURI(), e);

        model.addAttribute("errorCode", "UNEXPECTED_ERROR");
        model.addAttribute("errorMessage", "예상치 못한 오류가 발생했습니다");
        model.addAttribute("requestUrl", request.getRequestURI());

        return "error/500";
    }
}