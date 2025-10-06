package com.example.links.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.links.dto.CategoriaDto;
import com.example.links.dto.CategoriaUpdateDto;
import com.example.links.entity.Categoria;
import com.example.links.exception.ResourceNotFoundException;
import com.example.links.repository.CategoriaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final UserService userService;

    @Cacheable(value = "categorias", key = "T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().getName()")
    public List<Categoria> findAllByUserId() {
        Long userId = userService.getUserId();
        return categoriaRepository.findAllByUserId(userId);
    }

    @Transactional
    @CacheEvict(value = {
        "categorias", "categorias-list", "categoria"
    }, allEntries = true)
    public Categoria save(CategoriaDto dto){
        Categoria categoria = new Categoria();
        categoria.setName(dto.getName());
        categoria.setUser(userService.getUser()); 
        wipeCache();
        return categoriaRepository.save(categoria);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "categorias-list", key = "#ids")
    public List<Categoria> findAllByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        return categoriaRepository.findAllByIds(ids);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "categoria", key="#id")
    public Categoria findById(Long id) {
        return categoriaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada!"));
    }

    @Transactional
    @CacheEvict(value = {
        "categorias", "categorias-list", "categoria"
    }, allEntries = true)
    public boolean delete (Long id) {
        Categoria categoria = findById(id);
        categoriaRepository.delete(categoria);
        wipeCache();
        return !categoriaRepository.existsById(id);
    }

    @Transactional
    @CacheEvict(value = {
        "categorias", "categorias-list", "categoria"
    }, allEntries = true)
    public Categoria update (CategoriaUpdateDto dto) {
        Categoria categoria = findById(dto.getId());
        categoria.setName(dto.getName());
        wipeCache();
        return categoriaRepository.save(categoria);
    }

    @CacheEvict(value = {
        "categorias", "categorias-list", "categoria"
    }, allEntries = true)
    public void wipeCache (){
        System.out.println("Limpando cache...");
    }

}

