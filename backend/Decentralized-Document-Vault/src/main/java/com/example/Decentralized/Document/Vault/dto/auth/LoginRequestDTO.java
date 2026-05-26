package com.example.Decentralized.Document.Vault.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {


    @Email(message = "Email is invalid")
    private String email;

    @JsonProperty("password")
    @NotBlank(message = "Password cannot be blank")
    private String rawPassword;

}
