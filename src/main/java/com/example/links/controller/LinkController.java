package com.example.links.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.links.dto.LinkDto;
import com.example.links.entity.Link;
import com.example.links.service.CategoriaService;
import com.example.links.service.LinkService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/link")
@Log4j2
public class LinkController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private LinkService linkService;

    @GetMapping("")
    public ModelAndView getLinksPage(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/link/index");

        CsrfToken csrf = (CsrfToken) request.getAttribute("_csrf");

        if (csrf != null) {
            modelAndView.addObject("_csrf", csrf);
        }

        List<Link> links = linkService.findAllByUser();

        modelAndView.addObject("links", links);

        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView getLinkAddPage(HttpServletRequest request) {
        log.debug("GET /link/add called");
        ModelAndView modelAndView = new ModelAndView("/link/add");

        CsrfToken csrf = (CsrfToken) request.getAttribute("_csrf");
        if (csrf != null) {
            modelAndView.addObject("_csrf", csrf);
            log.debug("CSRF token found: parameterName={} tokenPresent={}", csrf.getParameterName(),
                    csrf.getToken() != null);
        } else {
            log.debug("No CSRF token in request attributes");
        }

        var categorias = categoriaService.findAllByUserId();
        var links = linkService.findAllByUser();

        log.debug("Categorias size: {}", categorias == null ? 0 : categorias.size());
        log.debug("Links size: {}", links == null ? 0 : links.size());

        modelAndView.addObject("listagem-categorias", categorias);
        modelAndView.addObject("links", links);

        if (!links.isEmpty()) {
            log.debug("Categorias do terceiro link: {}", links.get(2).getName());
        }

        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView saveLink(@Valid @ModelAttribute LinkDto dto, BindingResult result, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("redirect:/link/add");

        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");

        if (csrfToken != null) {
            modelAndView.addObject("_csrf", csrfToken);
        }

        if (result.hasErrors()) {
            result.getFieldErrors().forEach(error -> {
                modelAndView.addObject(error.getField().concat("Error"), error.getDefaultMessage());
            });
            return modelAndView;
        }

        Link link = linkService.save(dto);

        if (link == null) {
            modelAndView.addObject("error", "O link não pode ser salvo!");
            return modelAndView;
        }

        modelAndView.addObject("success", "Link adicionado com sucesso!");

        return modelAndView;
    }

}
