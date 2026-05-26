package com.example.Decentralized.Document.Vault.exception;

import com.example.Decentralized.Document.Vault.dto.exception.ApiErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice

public class GlobalExceptionHandler {
@ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidInputException(InvalidRequestException ex){
        return ResponseEntity.badRequest().body(ApiErrorResponse.builder().message(ex.getMessage()).timestamp(LocalDateTime.now()).build());

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        return ResponseEntity.badRequest().body(ApiErrorResponse.builder().message(ex.getMessage()).timestamp(LocalDateTime.now()).build());
    }


}
