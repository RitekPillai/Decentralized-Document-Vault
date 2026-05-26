package com.example.Decentralized.Document.Vault.services.auth;


import com.example.Decentralized.Document.Vault.dto.auth.LoginRequestDTO;
import com.example.Decentralized.Document.Vault.dto.auth.LoginResponseDTO;
import com.example.Decentralized.Document.Vault.dto.auth.SignupRequestDTO;
import com.example.Decentralized.Document.Vault.exception.InvalidRequestException;
import com.example.Decentralized.Document.Vault.model.auth.User;
import com.example.Decentralized.Document.Vault.repos.auth.UserRepo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private  final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final AuthUtil authUtil;

    public String registerUser(@Valid SignupRequestDTO signupRequestDTO){

        userRepo.findByEmail(signupRequestDTO.getEmail())
                .ifPresent(user -> {
                    throw new InvalidRequestException("You are already registered Try login");
                });
        User user = User.builder().userName(signupRequestDTO.getUserName()).email(signupRequestDTO.getEmail()).encryptedPassword(passwordEncoder.encode(signupRequestDTO.getPassword())).build();

        userRepo.save(user);
        return "you are registered successfully now you can login";
    }


    public LoginResponseDTO loginUser(@Valid LoginRequestDTO loginRequestDTO) {

        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getEmail(),
                            loginRequestDTO.getRawPassword()
                    )
            );

            User user = (User) authentication.getPrincipal();

            String jwtToken = authUtil.generateToken(user);

            return LoginResponseDTO.builder().jwtToken(jwtToken).build();

        } catch (Exception e) {
            log.info("Invalid email or password: {}", e.getMessage());
            throw new InvalidRequestException("Invalid email or password.");
        }

    }
}
