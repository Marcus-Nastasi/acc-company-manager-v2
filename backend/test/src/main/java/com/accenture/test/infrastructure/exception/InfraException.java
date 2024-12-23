package com.accenture.test.infrastructure.exception;

import java.io.Serial;

public class InfraException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InfraException(String message) {
        super(message);
    }
}
