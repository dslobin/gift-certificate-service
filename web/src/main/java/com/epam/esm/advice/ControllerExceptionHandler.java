package com.epam.esm.advice;

import com.epam.esm.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GiftCertificateNotFoundException.class)
    public ResponseEntity<Object> handleCertificateNotFoundException(
            GiftCertificateNotFoundException e,
            WebRequest request
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(ZonedDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(e.getMessage())
                .errorCode(getErrorCode(HttpStatus.NOT_FOUND, ResourceCode.GIFT_CERTIFICATE))
                .path(request.getDescription(false))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<Object> handleTagNotFoundException(
            TagNotFoundException e,
            WebRequest request
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(ZonedDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(e.getMessage())
                .errorCode(getErrorCode(HttpStatus.NOT_FOUND, ResourceCode.TAG))
                .path(request.getDescription(false))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(
            UserNotFoundException e,
            WebRequest request
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(ZonedDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(e.getMessage())
                .errorCode(getErrorCode(HttpStatus.NOT_FOUND, ResourceCode.USER_ACCOUNT))
                .path(request.getDescription(false))
                .build();
        ;

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NameAlreadyExistException.class)
    public ResponseEntity<Object> handleExistedNameException(
            NameAlreadyExistException e,
            WebRequest request
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(ZonedDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(e.getMessage())
                .errorCode(getErrorCode(HttpStatus.BAD_REQUEST, ResourceCode.TAG))
                .path(request.getDescription(false))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Object> handleOrderNotFoundException(
            OrderNotFoundException e,
            WebRequest request
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(ZonedDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(e.getMessage())
                .errorCode(getErrorCode(HttpStatus.NOT_FOUND, ResourceCode.ORDER))
                .path(request.getDescription(false))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(ZonedDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(errors)
                .errorCode(getErrorCode(HttpStatus.NOT_FOUND, ResourceCode.NOT_PROVIDED))
                .path(request.getDescription(false))
                .build();
        return new ResponseEntity<>(errorResponse, headers, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException e,
            WebRequest request
    ) {
        Set<String> messages = new HashSet<>();

        e.getConstraintViolations().forEach(violation -> {
            Path propertyPath = violation.getPropertyPath();
            String message = violation.getMessage();
            messages.add(propertyPath + ": " + message);
        });

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(ZonedDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(messages)
                .errorCode(getErrorCode(HttpStatus.BAD_REQUEST, ResourceCode.NOT_PROVIDED))
                .path(request.getDescription(false))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * API error code based on HTTP status and resource code
     *
     * @see HttpStatus
     * @see ResourceCode
     */
    private int getErrorCode(HttpStatus status, ResourceCode resourceCode) {
        String errorCode = String.valueOf(status.value()) + resourceCode.value();
        return Integer.parseInt(errorCode);
    }
}
