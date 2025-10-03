package com.example.links.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.links.entity.Categoria;
import com.example.links.entity.CustomUser;
import com.example.links.service.CategoriaService;
import com.example.links.service.UserService;

@Controller
@RequiredArgsConstructor
@Log4j2
public class DashboardController {

    private final UserService userService;
    private final CategoriaService categoriaService;

    @GetMapping("/dashboard")
    public ModelAndView getDashboard() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView mv = new ModelAndView("dashboard");
        CustomUser user = userService.loadUserByUsername(auth.getName());

        mv.addObject("nome", user.getName());
        List<Categoria> categorias = categoriaService.findAllByUserId();
        log.debug("Categorias", categorias);
        mv.addObject("categorias", categoriaService.findAllByUserId());
        return mv;
    }

}
