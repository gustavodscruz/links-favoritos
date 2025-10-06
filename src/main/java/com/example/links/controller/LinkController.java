package com.example.links.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.links.dto.LinkDto;
import com.example.links.dto.LinkPreviewRequest;
import com.example.links.dto.LinkUpdateDto;
import com.example.links.entity.CustomUser;
import com.example.links.entity.Link;
import com.example.links.helpers.CsrfHelper;
import com.example.links.messages.KafkaConsumer;
import com.example.links.messages.KafkaProducer;
import com.example.links.service.CategoriaService;
import com.example.links.service.LinkService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/link")
@Log4j2
public class LinkController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private LinkService linkService;

    @Autowired
    private KafkaProducer kafkaProducer;

    @GetMapping("/add")
    public ModelAndView getLinkAddPage(HttpServletRequest request) {
        log.debug("GET /link/add called");
        ModelAndView modelAndView = new ModelAndView("link/add");

        CsrfHelper.addCsrfToken(modelAndView, request);

        var categorias = categoriaService.findAllByUserId();

        modelAndView.addObject("listagem-categorias", categorias);

        return modelAndView;
    }

    @GetMapping("/edit")
    public ModelAndView getLinkPage(@RequestParam(value = "link") Long id, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("link/add");

        Link link = linkService.findById(id);

        CsrfHelper.addCsrfToken(modelAndView, request);

        var categorias = categoriaService.findAllByUserId();
        modelAndView.addObject("listagem-categorias", categorias);

        modelAndView.addObject("link", link);

        return modelAndView;
    }

    @PostMapping("/edit")
    public ModelAndView editLink(@Valid @ModelAttribute LinkUpdateDto dto, BindingResult result) {
        ModelAndView modelAndView;

        if (result.hasErrors()) {
            modelAndView = new ModelAndView("redirect:/link/edit");
            modelAndView.addObject("link", dto.getId());
            result.getFieldErrors().forEach(error -> {
                modelAndView.addObject(error.getField().concat("Error"), error.getDefaultMessage());
            });
            return modelAndView;
        }

        Link link = linkService.update(dto);

        if (link == null) {
            modelAndView = new ModelAndView("redirect:/link/edit");
            modelAndView.addObject("link", dto.getId());
            modelAndView.addObject("error", "Não foi possível editar o link");
            return modelAndView;
        }

        modelAndView = new ModelAndView("redirect:/link/");

        modelAndView.addObject("success", "Link editado com sucesso!");

        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView saveLink(@Valid @ModelAttribute LinkDto dto, BindingResult result, HttpServletRequest request) {
        ModelAndView modelAndView;

        if (result.hasErrors()) {
            modelAndView = new ModelAndView("link/add");
            result.getFieldErrors().forEach(error -> {
                modelAndView.addObject(error.getField().concat("Error"), error.getDefaultMessage());
            });
            CsrfHelper.addCsrfToken(modelAndView, request);
            return modelAndView;
        }

        Link link = linkService.save(dto);

        if (link == null) {
            modelAndView = new ModelAndView("link/add");
            modelAndView.addObject("error", "O link não pode ser salvo!");
            CsrfHelper.addCsrfToken(modelAndView, request);
            return modelAndView;
        }

        LinkPreviewRequest linkPreviewRequest = new LinkPreviewRequest().builder()
                .url(link.getUrl())
                .requestedAt(LocalDateTime.now())
                .userId(((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId())
                .build();

        kafkaProducer.sendLinkForPreview(linkPreviewRequest);

        ModelAndView success = new ModelAndView("redirect:/link/")
                .addObject("success", "Link adicionado com sucesso!");
        CsrfHelper.addCsrfToken(success, request);

        return success;
    }

    @PostMapping("/delete")
    public ModelAndView deleteLink(@RequestParam Long id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/link/");

        boolean deleted = linkService.delete(id);

        if (deleted) {
            modelAndView.addObject("success", "Link deletado com sucesso!");
        } else {
            modelAndView.addObject("error", "Link não pode ser deletado");
        }

        return modelAndView;
    }

    @GetMapping({ "", "/" })
    public ModelAndView listLinks(HttpServletRequest request,
            @RequestParam(required = false) String success,
            @RequestParam(required = false) String error,
            @RequestParam(value = "categoria", required = false) Long categoriaId) {

        ModelAndView modelAndView = new ModelAndView("link/index");

        CsrfHelper.addCsrfToken(modelAndView, request);

        modelAndView.addObject("listagem-categorias", categoriaService.findAllByUserId());

        if (success != null) {
            modelAndView.addObject("success", success);
        }
        if (error != null) {
            modelAndView.addObject("error", error);
        }

        if (categoriaId != null) {
            modelAndView.addObject("links", linkService.findAllByCategoria(categoriaId));
            modelAndView.addObject("filteredBy", categoriaId);
            return modelAndView;
        }
        modelAndView.addObject("links", linkService.findAllByUser());
        return modelAndView;
    }

}
