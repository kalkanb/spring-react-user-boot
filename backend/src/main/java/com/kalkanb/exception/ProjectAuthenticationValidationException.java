package com.kalkanb.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ProjectAuthenticationValidationException extends RuntimeException {
    private Map<String, String> fields;

    public ProjectAuthenticationValidationException(String message, Map<String, String> fields) {
        super(message);
        this.fields = fields;
    }

    public ProjectAuthenticationValidationException(String message, Map<String, String> fields, Throwable cause) {
        super(message, cause);
        this.fields = fields;
    }
}
