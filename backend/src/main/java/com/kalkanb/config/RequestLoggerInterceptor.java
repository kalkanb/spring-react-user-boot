package com.kalkanb.config;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class RequestLoggerInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = Logger.getLogger("Project Logger");

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception exception) {

        String requestStr = request.getRequestURI().replace("/api/", "");
        String requestMethod = request.getMethod();
        String queryString = request.getQueryString();
        String queryClause = StringUtils.hasLength(queryString) ? "?" + queryString : "";

        String message = requestMethod + " " + requestStr + queryClause;

        if (exception == null && response.getStatus() == HttpStatus.OK.value()) {
            LOGGER.log(Level.INFO, message + " OK");
        } else if (exception != null) {
            LOGGER.log(Level.SEVERE, message + " FAILED", exception);
        }
    }
}
