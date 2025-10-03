package com.example.links.controller;

import com.example.links.dto.CategoriaDto;
import com.example.links.entity.Categoria;
import com.example.links.service.CategoriaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/add")
    public ModelAndView getAddCategoriaPage(
            @RequestParam(required = false) Boolean success,
            HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/categoria/add");

        if (Boolean.TRUE.equals(success)) {
            modelAndView.addObject("success", true);
        }

        CsrfToken csrf = (CsrfToken) request.getAttribute("_csrf");

        if (csrf != null) {
            modelAndView.addObject("_csrf", csrf);
        }
        
        return modelAndView;
    }

    @PostMapping("/add") 
    public ModelAndView saveCategoria(@RequestParam String name) {
        
        CategoriaDto categoriaDto = new CategoriaDto(name);
        Categoria categoria = categoriaService.save(categoriaDto);

        if (categoria == null) {
            System.out.println("Categoria não foi salva!");
        }

        return new ModelAndView("redirect:/categoria/add?success=true");

    }
}
