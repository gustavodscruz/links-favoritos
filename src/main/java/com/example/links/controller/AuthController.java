package com.example.links.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.links.dto.RegisterEmailAndPassword;
import com.example.links.entity.CustomUser;
import com.example.links.helpers.CsrfHelper;
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

        CsrfHelper.addCsrfToken(modelAndView, request);

        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("register");

        CsrfHelper.addCsrfToken(modelAndView, request);

        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@Valid @ModelAttribute RegisterEmailAndPassword dto, BindingResult result,
            HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView("register");

        CsrfHelper.addCsrfToken(modelAndView, request);

        if (result.hasErrors()) {
            List<FieldError> objectErrors = result.getFieldErrors();
            objectErrors.forEach(error -> {
                modelAndView.addObject(
                        error
                                .getField()
                                .concat("Error"),
                        error.getDefaultMessage());
            });
            return modelAndView;
        }

        CustomUser customUser = userService.saveUser(dto);

        if (customUser == null) {
            modelAndView.addObject("error", "Erro ao registrar usuário. Tente novamente.");
            return modelAndView;
        }

        modelAndView.addObject("success", "Usuário registrado com sucesso!");

        return modelAndView;
    }

}
