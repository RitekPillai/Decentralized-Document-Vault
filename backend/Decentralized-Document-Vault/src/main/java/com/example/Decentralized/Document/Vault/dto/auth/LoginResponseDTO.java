package com.example.Decentralized.Document.Vault.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDTO {

    @JsonProperty("jwt_token")
    private  String jwtToken;

//    @JsonProperty("refresh_token")
//    private String refreshToken;
}
