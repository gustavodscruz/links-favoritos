package com.example.links.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

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

}
