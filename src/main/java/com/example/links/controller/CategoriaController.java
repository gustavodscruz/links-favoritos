package com.example.links.controller;

import com.example.links.dto.CategoriaDto;
import com.example.links.dto.CategoriaUpdateDto;
import com.example.links.entity.Categoria;
import com.example.links.entity.CustomUser;
import com.example.links.service.CategoriaService;
import com.example.links.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;


    @GetMapping({"/add", ""})
    public ModelAndView getAddCategoriaPage(
            @RequestParam(required = false) Boolean success,
            @RequestParam(required = false) Boolean deleteSuccess,
            @RequestParam(required = false) Boolean deleteError,
            @RequestParam(value = "categoria", required = false) Long id,
            HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("categoria/add");

        if (Boolean.TRUE.equals(success)) {
            modelAndView.addObject("success", "Categoria cadastrada com sucesso!");
        }

        if (Boolean.TRUE.equals(deleteSuccess)) {
            modelAndView.addObject("deleteSuccess", true);
        }

        if (Boolean.TRUE.equals(deleteError)) {
            modelAndView.addObject("deleteError", true);
        }

        CsrfToken csrf = (CsrfToken) request.getAttribute("_csrf");
        if (csrf != null) {
            modelAndView.addObject("_csrf", csrf);
        }

        if (id != null){
            modelAndView.addObject("categoria", categoriaService.findById(id));
        }

        List<Categoria> categorias = categoriaService.findAllByUserId();
        modelAndView.addObject("categorias", categorias);

        CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        modelAndView.addObject("user", user);
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

    @PostMapping("/delete")
    public ModelAndView deleteCategoria(@RequestParam Long id) {

        boolean result = categoriaService.delete(id);

        if (!result) {
            return new ModelAndView("redirect:/categoria/add?deleteError=true");
        }

        return new ModelAndView("redirect:/categoria/add?deleteSuccess=true");
    }

    @GetMapping("/edit")
    public ModelAndView getEditPage(@RequestParam(value = "categoria") Long id) {
        return new ModelAndView("redirect:/categoria/add")
            .addObject("categoria", id);
    }

    @PostMapping("/edit")
    public ModelAndView editCategoria(@Valid @ModelAttribute CategoriaUpdateDto dto, BindingResult result, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/categoria/add");
        
        CsrfToken csrf = (CsrfToken) request.getAttribute("_csrf");
        if (csrf != null) {
            modelAndView.addObject("_csrf", csrf);
        }

        List<Categoria> categorias = categoriaService.findAllByUserId();
        modelAndView.addObject("categorias", categorias);

        CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        modelAndView.addObject("user", user);

        if (result.hasErrors()){
            result.getFieldErrors().forEach(error -> {
                modelAndView.addObject(error.getField().concat("Error"), error.getDefaultMessage());
            });
            return modelAndView;
        }

        Categoria categoria = categoriaService.update(dto);

        if (categoria == null){
            modelAndView.addObject("error", "Não foi possível editar a categoria");
            return modelAndView;
        }

        modelAndView.addObject("success", "Categoria editada com sucesso");

        return modelAndView;
    }
    
    

}
