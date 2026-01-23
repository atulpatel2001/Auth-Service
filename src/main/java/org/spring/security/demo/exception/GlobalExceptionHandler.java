package org.spring.security.demo.exception;

import org.spring.security.demo.dto.ApiResponse;
import org.spring.security.demo.dto.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* ---------------- VALIDATION ---------------- */

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(err ->
                        errors.put(err.getField(), err.getDefaultMessage())
                );

        return ResponseEntity.badRequest().body(
                ApiResponse.<ErrorResponse>builder()
                        .success(false)
                        .message("Validation failed")
                        .data(
                                ErrorResponse.builder()
                                        .errorCode("VALIDATION_ERROR")
                                        .message("Invalid request data")
                                        .details(errors)
                                        .build()
                        )
                        .build()
        );
    }

    /* ---------------- BUSINESS ---------------- */

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleBusinessException(
            BusinessException ex) {

        return ResponseEntity.badRequest().body(
                ApiResponse.<ErrorResponse>builder()
                        .success(false)
                        .message(ex.getMessage())
                        .data(
                                ErrorResponse.builder()
                                        .errorCode(ex.getStatus().toString())
                                        .message(ex.getMessage())
                                        .build()
                        )
                        .build()
        );
    }

    /* ---------------- NOT FOUND ---------------- */

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleNotFoundException(
            ResourceNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.<ErrorResponse>builder()
                        .success(false)
                        .message(ex.getMessage())
                        .data(
                                ErrorResponse.builder()
                                        .errorCode("RESOURCE_NOT_FOUND")
                                        .message(ex.getMessage())
                                        .build()
                        )
                        .build()
        );
    }

    /* ---------------- DB CONSTRAINT ---------------- */

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleConstraintViolation(
            DataIntegrityViolationException ex) {

        return ResponseEntity.badRequest().body(
                ApiResponse.<ErrorResponse>builder()
                        .success(false)
                        .message("Database constraint violation")
                        .data(
                                ErrorResponse.builder()
                                        .errorCode("DB_CONSTRAINT_ERROR")
                                        .message("Duplicate or invalid data")
                                        .build()
                        )
                        .build()
        );
    }

    /* ---------------- TYPE MISMATCH ---------------- */

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        return ResponseEntity.badRequest().body(
                ApiResponse.<ErrorResponse>builder()
                        .success(false)
                        .message("Invalid parameter type")
                        .data(
                                ErrorResponse.builder()
                                        .errorCode("TYPE_MISMATCH")
                                        .message(
                                                "Parameter '" + ex.getName() +
                                                        "' has invalid value"
                                        )
                                        .build()
                        )
                        .build()
        );
    }

    /* ---------------- FALLBACK ---------------- */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleGenericException(
            Exception ex) {

        // LOG THIS (IMPORTANT)
        ex.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.<ErrorResponse>builder()
                        .success(false)
                        .message("Internal server error")
                        .data(
                                ErrorResponse.builder()
                                        .errorCode("500")
                                        .message(ex.getMessage())
                                        .build()
                        )
                        .build()
        );
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(
            AccessDeniedException ex) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.builder()
                        .success(false)
                        .message("ACCESS_DENIED")
                        .data(ErrorResponse.builder()
                                .errorCode("401")
                                .message("You do not have permission to perform this action")
                                .build())
                        .build());
    }


    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthorizationDenied(
            AuthorizationDeniedException ex) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.builder()
                        .success(false)
                        .message("Access denied")
                        .data(ErrorResponse.builder()
                                .errorCode("403")
                                .message("Insufficient privileges")
                                .build())
                        .build());
    }
}

