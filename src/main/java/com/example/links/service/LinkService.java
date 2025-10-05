package com.example.links.service;

import java.util.List;
import java.util.stream.LongStream;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.links.dto.LinkDto;
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
    public Link update(LinkDto dto, Long id) {
        Link link = findById(id);
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
    public boolean delete(Long id) {
        linkRepository.deleteById(id);
        wipeCache();
        if (linkRepository.existsById(id))
            return false;
        return true;
    }

    @CacheEvict(value = {
            "links", "link"
    })
    public void wipeCache() {
        System.out.println("Limpando cache...");
    }
}
