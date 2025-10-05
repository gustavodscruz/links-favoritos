package com.example.links.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @GetMapping("/error")
    public ModelAndView handleError(HttpServletRequest request) {

        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        Map<String, Object> attrs = errorAttributes.getErrorAttributes(new ServletWebRequest(request),
                ErrorAttributeOptions.defaults());

        return new ModelAndView("error")
                .addObject("status", statusCode)
                .addObject("message", attrs.get("message"));

    }
}