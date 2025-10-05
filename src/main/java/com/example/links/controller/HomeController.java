package com.example.links.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.links.helpers.CsrfHelper;

import jakarta.servlet.http.HttpServletRequest;

@RequestMapping({ "/", "" })
@Controller
public class HomeController {
    @GetMapping("")
    public ModelAndView getHomePage(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("index");
        CsrfHelper.addCsrfToken(modelAndView, request);
        return modelAndView;
    }
}
