package com.example.Decentralized.Document.Vault.controllers;

import com.example.Decentralized.Document.Vault.dto.auth.LoginRequestDTO;
import com.example.Decentralized.Document.Vault.dto.auth.LoginResponseDTO;
import com.example.Decentralized.Document.Vault.dto.auth.SignupRequestDTO;
import com.example.Decentralized.Document.Vault.services.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignupRequestDTO signupRequestDTO){


    return ResponseEntity.ok(authService.registerUser(signupRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO){
        return ResponseEntity.ok(authService.loginUser(loginRequestDTO));
    }
}
