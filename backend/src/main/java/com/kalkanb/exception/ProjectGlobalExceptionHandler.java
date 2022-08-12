package com.kalkanb.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@ControllerAdvice
public class ProjectGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String PARAMETER_ERROR = "Invalid or missing parameters";
    private static final Logger LOGGERR = Logger.getLogger("Project Logger");

    @ExceptionHandler({ProjectException.class})
    public ResponseEntity<ProjectErrorResponse> handleProjectException(ProjectException ex, WebRequest request) {

        ProjectErrorResponse errorResponse = new ProjectErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setErrorType(ex.getClass().getSimpleName());

        writeExceptionLog(ex, request);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ProjectValidationException.class})
    public ResponseEntity<ProjectErrorResponse> handleProjectValidationException(ProjectValidationException ex, WebRequest request) {
        ProjectErrorResponse errorResponse = new ProjectErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setErrorType(ex.getClass().getSimpleName());

        writeExceptionLog(ex, request);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ProjectNotFoundException.class})
    public ResponseEntity<ProjectErrorResponse> handleProjectNotFoundException(ProjectNotFoundException ex, WebRequest request) {
        ProjectErrorResponse errorResponse = new ProjectErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setErrorType(ex.getClass().getSimpleName());

        writeExceptionLog(ex, request);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ProjectArgumentValidationException.class})
    public ResponseEntity<ProjectErrorResponse> handleProjectArgumentValidationException(ProjectArgumentValidationException ex, WebRequest request) {
        ProjectErrorResponse errorResponse = new ProjectErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setErrorType(ex.getClass().getSimpleName());
        errorResponse.setFields(ex.getFields());

        writeExceptionLog(ex, request);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ProjectAuthenticationValidationException.class})
    public ResponseEntity<ProjectErrorResponse> handleProjectAuthenticationValidationException(ProjectAuthenticationValidationException ex, WebRequest request) {
        ProjectErrorResponse errorResponse = new ProjectErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        errorResponse.setErrorType(ex.getClass().getSimpleName());
        errorResponse.setFields(ex.getFields());

        writeExceptionLog(ex, request);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ProjectErrorResponse errorResponse = new ProjectErrorResponse();

        Map<String, String> fields = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                fields.put(fieldError.getField(), fieldError.getDefaultMessage())
        );
        errorResponse.setFields(fields);
        errorResponse.setMessage(PARAMETER_ERROR);
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setErrorType(ex.getClass().getSimpleName());

        writeExceptionLog(ex, request);

        return new ResponseEntity<>(errorResponse, headers, status);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {
        ProjectErrorResponse errorResponse = new ProjectErrorResponse();

        Map<String, String> fields = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                fields.put(fieldError.getField(), fieldError.getDefaultMessage())
        );
        errorResponse.setFields(fields);
        errorResponse.setMessage(PARAMETER_ERROR);
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setErrorType(ex.getClass().getSimpleName());

        writeExceptionLog(ex, request);

        return new ResponseEntity<>(errorResponse, headers, status);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          @NonNull HttpHeaders headers,
                                                                          @NonNull HttpStatus status,
                                                                          @NonNull WebRequest request) {
        ProjectErrorResponse errorResponse = new ProjectErrorResponse();

        Map<String, String> fields = new HashMap<>();
        fields.put(ex.getParameterName(), "parametre zorunludur");
        errorResponse.setFields(fields);
        errorResponse.setMessage(PARAMETER_ERROR);
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setErrorType(ex.getClass().getSimpleName());

        writeExceptionLog(ex, request);

        return new ResponseEntity<>(errorResponse, headers, status);
    }

    private void writeExceptionLog(Exception ex, WebRequest request) {
        ServletWebRequest servletWebRequest = ((ServletWebRequest) request);
        HttpServletRequest httpServletRequest = servletWebRequest.getRequest();
        String requestMethod = httpServletRequest.getMethod();
        String requestStr = httpServletRequest.getRequestURI();
        String queryString = httpServletRequest.getQueryString();
        String queryClause = StringUtils.hasLength(queryString) ? "?" + queryString : "";

        String message = requestMethod + " " + requestStr + queryClause + " FAILED";

        LOGGERR.log(Level.SEVERE, message, ex);
    }
}
