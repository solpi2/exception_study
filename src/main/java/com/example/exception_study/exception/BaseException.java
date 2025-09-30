package com.example.exception_study.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {
  private final String errorCode;
  private final HttpStatus httpStatus;

  protected BaseException(String errorCode, String message, HttpStatus httpStatus) {
    super(message);
    this.errorCode = errorCode;
    this.httpStatus = httpStatus;
  }

  protected BaseException(String errorCode, String message, HttpStatus httpStatus, Throwable cause) {
    super(message);
    this.errorCode = errorCode;
    this.httpStatus = httpStatus;
  }
}