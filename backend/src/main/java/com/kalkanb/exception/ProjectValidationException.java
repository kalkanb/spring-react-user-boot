package com.kalkanb.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectValidationException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Unknown Error";

    public ProjectValidationException() {
        super(DEFAULT_MESSAGE);
    }

    public ProjectValidationException(String message) {
        super(message);
    }

    public ProjectValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProjectValidationException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
