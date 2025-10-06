package com.example.links.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.links.helpers.CsrfHelper;
import com.example.links.service.NotificationService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notifications")
    public ModelAndView getNotificationsPage(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("notifications");

        CsrfHelper.addCsrfToken(modelAndView, request);
        modelAndView.addObject("notificacoes", notificationService.getNotifications());

        return modelAndView;
    }

}
