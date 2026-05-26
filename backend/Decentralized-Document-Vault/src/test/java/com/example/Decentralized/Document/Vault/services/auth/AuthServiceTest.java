package com.example.Decentralized.Document.Vault.services.auth;

import com.example.Decentralized.Document.Vault.dto.auth.LoginRequestDTO;
import com.example.Decentralized.Document.Vault.dto.auth.LoginResponseDTO;
import com.example.Decentralized.Document.Vault.dto.auth.SignupRequestDTO;
import com.example.Decentralized.Document.Vault.exception.InvalidRequestException;
import com.example.Decentralized.Document.Vault.model.auth.User;
import com.example.Decentralized.Document.Vault.repos.auth.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AuthUtil authUtil;

    @InjectMocks
    private AuthService authService;

    private SignupRequestDTO signupRequestDTO;
    private LoginRequestDTO loginRequestDTO;
    private User testUser;

    @BeforeEach
    void setUp() {
        signupRequestDTO = new SignupRequestDTO();
        signupRequestDTO.setUserName("tester");
        signupRequestDTO.setEmail("test@test.com");
        signupRequestDTO.setPassword("rawPassword123");

        loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("test@test.com");
        loginRequestDTO.setRawPassword("rawPassword123");

        testUser = User.builder()
                .userName("tester")
                .email("test@test.com")
                .encryptedPassword("encodedPassword123")
                .build();
    }


    @Test
    void registerUser_Success_ReturnsSuccessMessage() {
        when(userRepo.findByEmail(signupRequestDTO.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(signupRequestDTO.getPassword())).thenReturn("encodedPassword123");

        String result = authService.registerUser(signupRequestDTO);

        assertEquals("you are registered successfully now you can login", result);
        verify(userRepo, times(1)).findByEmail(signupRequestDTO.getEmail());
        verify(userRepo, times(1)).save(any(User.class));}

    @Test
    void registerUser_EmailAlreadyExists_ThrowsInvalidRequestException() {
        when(userRepo.findByEmail(signupRequestDTO.getEmail())).thenReturn(Optional.of(testUser));

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> authService.registerUser(signupRequestDTO));

        assertEquals("You are already registered Try login", exception.getMessage());

        verify(userRepo, never()).save(any(User.class));
    }


    @Test
    void loginUser_Success_ReturnsJwtToken() {
        String expectedToken = "mock.jwt.token.123";
        Authentication authenticationMock = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authenticationMock);

        when(authenticationMock.getPrincipal()).thenReturn(testUser);

        when(authUtil.generateToken(testUser)).thenReturn(expectedToken);

        LoginResponseDTO responseDTO = authService.loginUser(loginRequestDTO);

        assertNotNull(responseDTO);
        assertEquals(expectedToken, responseDTO.getJwtToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authUtil, times(1)).generateToken(testUser);
    }

    @Test
    void loginUser_Failure_BadCredentialsThrowsInvalidRequestException() {

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> authService.loginUser(loginRequestDTO));

        assertEquals("Invalid email or password.", exception.getMessage());

        verify(authUtil, never()).generateToken(any());
    }
}