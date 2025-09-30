package com.example.exception_study.exception;

import lombok.Getter;

@Getter
public class ExternalServiceException extends SystemException {

    private final String serviceName;

    public ExternalServiceException(String serviceName, String message) {
        super("EXTERNAL_SERVICE_ERROR",
                String.format("외부 서비스 오류 [%s]: %s", serviceName, message));
        this.serviceName = serviceName;
    }

    public ExternalServiceException(String serviceName, String message, Throwable cause) {
        super(String.format("외부 서비스 오류 [%s]: %s", serviceName, message), cause);
        this.serviceName = serviceName;
    }
}