package com.example.links.helpers;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

public class CsrfHelper {

    public static void addCsrfToken(ModelAndView modelAndView, HttpServletRequest request) {
        CsrfToken csrf = (CsrfToken) request.getAttribute("_csrf");
        if (csrf != null) {
            modelAndView.addObject("_csrf", csrf);
        }
    }
}
