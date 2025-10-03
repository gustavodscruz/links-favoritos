package com.example.links.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class CategoriaDto {
    private String name;

    public CategoriaDto(String name) {
    }
}
