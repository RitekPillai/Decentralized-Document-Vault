package com.example.Decentralized.Document.Vault.dto.exception;




import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder

public class ApiErrorResponse {

    private String message; /// show to the user

    private String error;





    private LocalDateTime timestamp;


}
