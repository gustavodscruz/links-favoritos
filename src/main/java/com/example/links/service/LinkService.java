package com.example.links.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.links.dto.LinkDto;
import com.example.links.entity.Categoria;
import com.example.links.entity.CustomUser;
import com.example.links.entity.Link;
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

    public Link save(LinkDto dto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUser user = ((CustomUser) principal);

        Link link = new Link(dto);
        link.setUser(user);

        List<Categoria> categorias = categoriaService.findAllByIds(dto.getCategorias());
        link.setCategorias(categorias);

        return linkRepository.save(link);
    }
}
