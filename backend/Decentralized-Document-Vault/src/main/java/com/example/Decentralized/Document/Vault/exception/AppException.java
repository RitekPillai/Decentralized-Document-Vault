package com.example.Decentralized.Document.Vault.exception;

import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException{

    public AppException(String message, HttpStatus status){
        super(message);
    }
}
