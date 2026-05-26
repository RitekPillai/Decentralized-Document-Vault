package com.example.Decentralized.Document.Vault.controllers;

import com.example.Decentralized.Document.Vault.dto.auth.LoginRequestDTO;
import com.example.Decentralized.Document.Vault.dto.auth.LoginResponseDTO;
import com.example.Decentralized.Document.Vault.dto.auth.SignupRequestDTO;
import com.example.Decentralized.Document.Vault.filters.JWTFilters;
import com.example.Decentralized.Document.Vault.services.auth.AuthService;
import com.example.Decentralized.Document.Vault.services.auth.AuthUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JWTFilters jwtFilters;

    @MockitoBean
    private AuthUtil authUtil;

    private SignupRequestDTO signupRequestDTO;
    private LoginRequestDTO loginRequestDTO;

    @BeforeEach
    void setUp() {
        signupRequestDTO = new SignupRequestDTO();
        signupRequestDTO.setUserName("tester");
        signupRequestDTO.setEmail("test@test.com");
        signupRequestDTO.setPassword("RawPassword@123");

        loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("test@test.com");
        loginRequestDTO.setRawPassword("RawPassword@123");
    }


    @Test
    void signUp_ValidRequest_Returns200Ok() throws Exception {
        String successMessage = "you are registered successfully now you can login";
        when(authService.registerUser(any(SignupRequestDTO.class))).thenReturn(successMessage);

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(successMessage));
    }

@Test
void signUp_InvalidRequest_Returns400BadRequest() throws Exception {
   signupRequestDTO.setPassword("invalid password");
    mockMvc.perform(post("/api/v1/auth/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(signupRequestDTO)))
            .andExpect(status().isBadRequest());

}

    @Test
    void login_ValidRequest_Returns200OkWithToken() throws Exception {
        String mockToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.mock.token";
        LoginResponseDTO responseDTO = LoginResponseDTO.builder().jwtToken(mockToken).build();

        when(authService.loginUser(any(LoginRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt_token").value(mockToken));
    }

    @Test
    void login_InvalidRequest_Returns400BadRequest() throws Exception {

        loginRequestDTO.setRawPassword(null);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isBadRequest());
    }
}