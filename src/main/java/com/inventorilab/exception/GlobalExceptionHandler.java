package com.inventorilab.exception;

import com.inventorilab.response.WebResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.dao.DataIntegrityViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<WebResponse<String>> handleResourceNotFound(ResourceNotFoundException ex) {
        WebResponse<String> response = WebResponse.<String>builder()
                .code(HttpStatus.NOT_FOUND.value())
                .status("NOT FOUND")
                .message(ex.getMessage())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<WebResponse<String>> handleBadRequest(BadRequestException ex) {
        WebResponse<String> response = WebResponse.<String>builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .status("BAD REQUEST")
                .message(ex.getMessage())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<WebResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        WebResponse<Map<String, String>> response = WebResponse.<Map<String, String>>builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .status("BAD REQUEST")
                .message("Validation failed")
                .data(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<WebResponse<String>> handleBadCredentials(BadCredentialsException ex) {
        WebResponse<String> response = WebResponse.<String>builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .status("UNAUTHORIZED")
                .message("Email atau password salah!")
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<WebResponse<String>> handleAccessDenied(AccessDeniedException ex) {
        WebResponse<String> response = WebResponse.<String>builder()
                .code(HttpStatus.FORBIDDEN.value())
                .status("FORBIDDEN")
                .message("Anda tidak memiliki akses ke endpoint ini!")
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<WebResponse<String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        WebResponse<String> response = WebResponse.<String>builder()
                .code(HttpStatus.CONFLICT.value())
                .status("CONFLICT")
                .message("Data tidak dapat dihapus karena masih terkait dengan data lain!")
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<WebResponse<String>> handleGeneralException(Exception ex) {
        WebResponse<String> response = WebResponse.<String>builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .status("INTERNAL SERVER ERROR")
                .message("Terjadi kesalahan internal pada server: " + ex.getMessage())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
