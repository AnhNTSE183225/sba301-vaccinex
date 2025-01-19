package com.vaccine.exception;

import com.vaccine.dto.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> unauthorizedException(AuthenticationException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(new Date(), "Failed", "You don't have permission", e.getMessage(), request.getDescription(false).replace("uri=", "")));
    }

    @ExceptionHandler(ElementNotFoundException.class)
    public ResponseEntity<ErrorResponse> elementNotFoundException(AuthenticationException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(new Date(), "Failed", "Can not found", e.getMessage(), request.getDescription(false).replace("uri=", "")));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> unauthorizedExceptionJWT(AuthenticationException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(new Date(), "Failed", "You don't have permission", e.getMessage(), request.getDescription(false).replace("uri=", "")));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(new Date(), "Failed", "Some arguments are invalid", errors, request.getDescription(false).replace("uri=", "")));
    }

}