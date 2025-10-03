package com.example.links.entity;

import java.util.List;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Size(max = 50, message = "Nome do link deve ter no máximo 50 caracteres")
    private String name;

    private String url;
    
    private boolean favorited;

    @Nullable
    private String thumbnailPreview;

    @Nullable
    private String titlePreview;
    
    @ManyToMany
    private List<Categoria> categorias;  

    @ManyToOne
    @JoinTable(name = "user_id")
    private CustomUser user;
}
