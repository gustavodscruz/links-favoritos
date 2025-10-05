package com.example.links.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/link")
@Log4j2
public class LinkController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private LinkService linkService;

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

        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView saveLink(@Valid @ModelAttribute LinkDto dto, BindingResult result) {
        ModelAndView modelAndView = new ModelAndView("redirect:/link/add");

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

    @PostMapping("/delete")
    public ModelAndView deleteLink(@RequestParam Long id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/link/");

        boolean deleted = linkService.delete(id);

        if (deleted) {
            modelAndView.addObject("deletedSuccess", "Link deletado com sucesso!");
        } else {
            modelAndView.addObject("deletedError", "Link não pode ser deletado");
        }

        return modelAndView;
    }

    @GetMapping({ "", "/" })
    public ModelAndView listLinks(HttpServletRequest request,
            @RequestParam(value = "deletedSuccess", required = false) String deletedSuccess,
            @RequestParam(value = "deletedError", required = false) String deletedError,
            @RequestParam(value = "categoria", required = false) Long categoriaId
            ) {

        ModelAndView modelAndView = new ModelAndView("/link/index");
        CsrfToken csrf = (CsrfToken) request.getAttribute("_csrf");

        modelAndView.addObject("listagem-categorias", categoriaService.findAllByUserId());

        if (csrf != null) {
            modelAndView.addObject("_csrf", csrf);
        }
        if (deletedSuccess != null) {
            modelAndView.addObject("deletedSuccess", deletedSuccess);
        }
        if (deletedError != null) {
            modelAndView.addObject("deletedError", deletedError);
        }

        if (categoriaId != null) {
            modelAndView.addObject("links", linkService.findAllByCategoria(categoriaId));
            modelAndView.addObject("filteredBy", categoriaId);
            return modelAndView;
        }
        modelAndView.addObject("links", linkService.findAllByUser());
        return modelAndView;
    }
    

}
