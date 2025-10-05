package com.example.links.service;

import java.util.List;
import java.util.stream.LongStream;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.links.dto.LinkDto;
import com.example.links.dto.LinkUpdateDto;
import com.example.links.entity.Categoria;
import com.example.links.entity.CustomUser;
import com.example.links.entity.Link;
import com.example.links.exception.ResourceNotFoundException;
import com.example.links.repository.LinkRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LinkService {
    private final LinkRepository linkRepository;

    private final CategoriaService categoriaService;

    @Transactional(readOnly = true)
    @Cacheable(value = "links", key = "T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().getName()")
    public List<Link> findAllByUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((CustomUser) principal).getId();

        return linkRepository.findAllByUserId(userId);
    }

    public List<Link> findAllByCategoria(Long categoriaId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((CustomUser) principal).getId();

        return linkRepository.findByCategoriasAndUser(categoriaId, userId);
    }

    @Transactional
    @CacheEvict(value = {
            "links", "link"
    }, allEntries = true)
    public Link save(LinkDto dto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUser user = ((CustomUser) principal);

        Link link = new Link(dto);
        link.setUser(user);

        List<Categoria> categorias = categoriaService.findAllByIds(dto.getCategorias());
        link.setCategorias(categorias);
        wipeCache();
        return linkRepository.save(link);
    }

    @Cacheable(value = "link", key = "#id")
    public Link findById(Long id) {
        return linkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Link não encontrado"));
    }

    @Transactional
    @CacheEvict(value = {
            "links", "link"
    }, allEntries = true)
    public Link update(LinkUpdateDto dto) {
        Link link = findById(dto.getId());
        link.setName(dto.getName());
        link.setUrl(dto.getUrl());
        link.setFavorited(dto.isFavorited());
        link.setDescription(dto.getDescription());
        List<Categoria> categorias = categoriaService.findAllByIds(dto.getCategorias());
        link.setCategorias(categorias);
        wipeCache();
        return linkRepository.save(link);
    }

    @Transactional
    @CacheEvict(value = {
            "links", "link"
    }, allEntries = true)
    public boolean delete(Long id) {
        Link link = findById(id);
        linkRepository.delete(link);
        wipeCache();
        return !linkRepository.existsById(id);

    }

    @CacheEvict(value = {
            "links", "link"
    }, allEntries = true)
    public void wipeCache() {
        System.out.println("Limpando cache...");
    }
}
