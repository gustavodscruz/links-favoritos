package com.example.links.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class LoginEmailAndPassword {
    @Email(message = "Insira um email válido")
    private String email;
    private String password;
}
