package com.example.links.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterEmailAndPassword {
    @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres.")
    private String name;
    @Email(message = "deve ser um email válido")
    private String email;

    @Size(min = 8, message = "Senha deve ter pelo menos 8 caracteres")
    private String password;

}
