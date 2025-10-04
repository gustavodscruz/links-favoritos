package com.example.links.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.links.service.CategoriaService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/link")
public class LinkController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/add")
    public ModelAndView getLinkAddPage(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/link/add");

        CsrfToken csrf = (CsrfToken) request.getAttribute("_csrf");
        if (csrf != null) {
            modelAndView.addObject("_csrf", csrf);
        }

        modelAndView.addObject("categorias", categoriaService.findAllByUserId());

        return modelAndView;
    }

}
