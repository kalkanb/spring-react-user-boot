package com.kalkanb.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Unknown Error";

    public ProjectException() {
        super(DEFAULT_MESSAGE);
    }

    public ProjectException(String message) {
        super(message);
    }

    public ProjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProjectException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
