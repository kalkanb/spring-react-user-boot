package com.kalkanb.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Unknown Error";

    public ProjectNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public ProjectNotFoundException(String message) {
        super(message);
    }

    public ProjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProjectNotFoundException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
