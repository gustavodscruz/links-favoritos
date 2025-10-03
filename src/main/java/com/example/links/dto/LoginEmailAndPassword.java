package com.example.links.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginEmailAndPassword {
    @Email(message = "Insira um email válido")
    private String email;
    @Size(min = 3, max = 12, message = "Senha deve conter entre 3 e 12 caracteres")
    private String password;
}
