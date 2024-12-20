package com.accenture.test.adapter.exception;

import com.accenture.test.application.exception.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Object> handleAppException(AppException exception, WebRequest request) {
        return ResponseEntity.badRequest().body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleUnknownRuntime(RuntimeException exception, WebRequest request) {
        return ResponseEntity.internalServerError().body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnknownError(Exception exception, WebRequest request) {
        return ResponseEntity.internalServerError().body(Map.of("error", exception.getMessage()));
    }
}
