package com.banco.bank_system.presentation.exception;

import com.banco.bank_system.application.exception.ApplicationException;
import com.banco.bank_system.application.exception.ResourceAlreadyExistsException;
import com.banco.bank_system.application.exception.ResourceNotFoundException;
import com.banco.bank_system.domain.exception.DomainException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now().format(formatter),
                status.value(),
                status.getReasonPhrase(),
                ex.getCode(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handle(
            ResourceAlreadyExistsException ex,
            HttpServletRequest request
    ) {

        HttpStatus status = HttpStatus.CONFLICT;

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now().format(formatter),
                status.value(),
                status.getReasonPhrase(),
                ex.getCode(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handle(
            ApplicationException ex,
            HttpServletRequest request
    ) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now().format(formatter),
                status.value(),
                status.getReasonPhrase(),
                ex.getCode(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handle(
            DomainException ex,
            HttpServletRequest request
    ) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now().format(formatter),
                status.value(),
                status.getReasonPhrase(),
                ex.getCode(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(response);
    }
}
