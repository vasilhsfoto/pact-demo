package com.vassilis.customerservice.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.vassilis.customerservice.dto.RestApiError;
import com.vassilis.customerservice.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;

import static java.util.stream.Collectors.joining;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionControllerAdvice extends ResponseEntityExceptionHandler {
    private static final String MESSAGE_PATTERN = "Exception '{}' encountered while processing request '{}'.";
    private static final String VALIDATION_MESSAGE_PATTERN = "Validation exception '{}' encountered while " +
            "processing request '{}'.";

    @ExceptionHandler
    public ResponseEntity<RestApiError> handleException(Exception exception,
                                                        HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        logException(exception, request.getRequestURI(), status);
        RestApiError restApiError = RestApiError.builder().detail("Internal Server Error").build();
        return ResponseEntity.status(status).body(restApiError);
    }

    @ExceptionHandler
    public ResponseEntity<RestApiError> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                        HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        logException(exception, request.getRequestURI(), status);
        RestApiError restApiError = RestApiError.builder().detail(exception.getMessage()).build();
        return ResponseEntity.status(status).body(restApiError);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        String errMsg = extractErrorMessageFromValidation(ex);
        log.warn(VALIDATION_MESSAGE_PATTERN, errMsg, extractUriPath(request));
        RestApiError restApiError = RestApiError.builder().detail(errMsg).build();
        return ResponseEntity.status(status).body(restApiError);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        logException(exception, extractUriPath(request), status);
        return super.handleExceptionInternal(
                exception,
                RestApiError.builder().detail(exception.getMessage()).build(),
                headers,
                status,
                request);
    }

    private void logException(Exception exception, String requestUri, HttpStatus status) {
        if (status.is5xxServerError()) {
            log.error(MESSAGE_PATTERN, exception, requestUri, exception);
        } else {
            log.warn(MESSAGE_PATTERN, exception, requestUri, exception);
        }
    }

    private String extractErrorMessageFromValidation(MethodArgumentNotValidException ex) {
        return ex.getAllErrors().stream().map(this::getErrorMessage).collect(joining(System.lineSeparator()));
    }

    private String getErrorMessage(ObjectError error) {
        if (error instanceof FieldError) {
            return String.format("%s:%s", ((FieldError) error).getField(), error.getDefaultMessage());
        } else {
            return error.getDefaultMessage();
        }
    }

    private String extractUriPath(WebRequest webRequest) {
        return Optional.of(webRequest)
                .filter(ServletWebRequest.class::isInstance)
                .map(ServletWebRequest.class::cast)
                .map(ServletWebRequest::getRequest)
                .map(HttpServletRequest::getRequestURI).orElse("Uri path is unknown");
    }

}
