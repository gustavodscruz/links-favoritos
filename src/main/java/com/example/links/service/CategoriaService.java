package com.example.links.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.links.dto.CategoriaDto;
import com.example.links.entity.Categoria;
import com.example.links.repository.CategoriaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final UserService userService;

    @Cacheable(value = "categorias", key = "#root.target.userService.getUserId()")
    public List<Categoria> findAllByUserId() {
        Long userId = userService.getUserId();
        return categoriaRepository.findAllByUserId(userId);
    }

    public Categoria save(CategoriaDto dto){
        Categoria categoria = new Categoria();
        categoria.setName(dto.getName());
        categoria.setUser(userService.getUser()); 
        return categoriaRepository.save(categoria);
    }

    public boolean delete (Long id) {
        categoriaRepository.deleteById(id);
        return true;
    }

}

