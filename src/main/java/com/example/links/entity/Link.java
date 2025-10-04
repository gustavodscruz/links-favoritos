package com.example.links.entity;

import java.util.List;

import com.example.links.dto.LinkDto;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Link {
    
    public Link(){

    }
    public Link(LinkDto dto) {
        this.name = dto.getName();
        this.favorited = dto.isFavorited();
        this.description = dto.getDescription();
        this.url = dto.getUrl();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Size(max = 50, message = "Nome do link deve ter no máximo 50 caracteres")
    private String name;

    private String url;
    
    private boolean favorited;

    private String description;

    @Nullable
    private String thumbnailPreview;

    @Nullable
    private String titlePreview;
    
    @ManyToMany
    @JoinTable(
        name = "link_categorias",
        joinColumns = @JoinColumn (name = "link_id"),
        inverseJoinColumns = @JoinColumn (name = "categoria_id")
    )
    private List<Categoria> categorias;  

    @ManyToOne
    @JoinColumn(name = "user_id")
    private CustomUser user;
}
