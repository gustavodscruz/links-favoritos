package com.example.links.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.links.dto.RegisterEmailAndPassword;
import com.example.links.entity.CustomUser;
import com.example.links.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public ModelAndView login(
            HttpServletRequest request,
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout) {
        ModelAndView modelAndView = new ModelAndView("login");

        if (error != null) {
            modelAndView.addObject("error", error);
        }

        if (logout != null) {
            modelAndView.addObject("logout", logout);
        }

        CsrfToken csrf = (CsrfToken) request.getAttribute("_csrf");

        if (csrf != null) {
            modelAndView.addObject("_csrf", csrf);
        }

        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("register");

        CsrfToken csrf = (CsrfToken) request.getAttribute("_csrf");

        if (csrf != null) {
            modelAndView.addObject("_csrf", csrf);
        }

        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@Valid @ModelAttribute RegisterEmailAndPassword dto, BindingResult result, HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView("register");

        if (result.hasErrors()) {
            modelAndView.addObject("errors", result.getAllErrors());
            return modelAndView;
        }

        CustomUser customUser = userService.saveUser(dto);

        if (customUser == null) {
            modelAndView.addObject("error", "Erro ao registrar usuário. Tente novamente.");
            return modelAndView;
        }

         CsrfToken csrf = (CsrfToken) request.getAttribute("_csrf");

        if (csrf != null) {
            modelAndView.addObject("_csrf", csrf);
        }


        modelAndView.addObject("success", "Usuário registrado com sucesso!");
        return modelAndView;
    }

}
