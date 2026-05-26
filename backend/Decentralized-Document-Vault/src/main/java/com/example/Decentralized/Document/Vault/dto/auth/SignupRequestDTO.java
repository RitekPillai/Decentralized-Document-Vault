package com.example.Decentralized.Document.Vault.dto.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDTO {

    @NotBlank(message = "Username can not be null")
    private String userName;

    @Email(message = "Email is invalid")
    private String email;
    @NotBlank(message = "New password cannot be blank")

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,50}$",
            message = "Password must be 8-50 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character (@#$%^&+=!)")
    private String password;


}
