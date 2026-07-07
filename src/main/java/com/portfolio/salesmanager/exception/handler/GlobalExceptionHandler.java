package com.portfolio.salesmanager.exception.handler;

import com.portfolio.salesmanager.dto.response.ErrorResponse;
import com.portfolio.salesmanager.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            UserNotFoundException.class,
            BranchNotFoundException.class,
            CategoryNotFoundException.class,
            ProductNotFoundException.class,
            InventoryNotFoundException.class,
            SaleNotFoundException.class,
            UsernameNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException exception) {

        ErrorResponse error = ErrorResponse.builder()
                .code("NOT_FOUND")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler({
            UserAlreadyExistsException.class,
            BranchAlreadyExistsException.class,
            CategoryAlreadyExistsException.class,
            InventoryAlreadyExistsException.class
    })
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException exception) {

        ErrorResponse error = ErrorResponse.builder()
                .code("CONFLICT")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler({
            StockInsufficientException.class,
            SaleAlreadyCancelledException.class,
            InvalidPasswordException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException exception) {

        ErrorResponse error = ErrorResponse.builder()
                .code("BAD_REQUEST")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler({
            AccessDeniedException.class,
            UserDisabledException.class
    })
    public ResponseEntity<ErrorResponse> handleForbidden(RuntimeException exception) {

        ErrorResponse error = ErrorResponse.builder()
                .code("FORBIDDEN")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception) {

        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("VALIDATION_ERROR")
                .message("Error de validación")
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException exception) {

        ErrorResponse error = ErrorResponse.builder()
                .code("INVALID_PARAMETER")
                .message("Parámetro inválido: " + exception.getName())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception exception) {

        ErrorResponse error = ErrorResponse.builder()
                .code("INTERNAL_SERVER_ERROR")
                .message("Ocurrió un error inesperado")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}