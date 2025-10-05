package com.example.links.dto;

import java.util.List;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LinkUpdateDto {
    @NotNull
    private Long id;
    @Size(min = 3, message = "O nome do link deve ter no mínimo 3 caracteres")
    private String name;
    @Pattern(regexp = "^(https?|ftp)://[^\s/$.?#].[^\s]*$", message = "URL inválida")
    private String url;
    private boolean favorited;
    @Nullable
    private String description;
    @NotEmpty(message = "Deve-se ter pelo menos uma categoria selecionada")
    private List<Long> categorias;
}
