package com.example.myhome.home.controller;

import com.example.myhome.home.model.PageRoleDisplay;
import com.example.myhome.home.repository.PageRoleDisplayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private PageRoleDisplayRepository repository;

    @ModelAttribute
    public void addUserAuthority(Model model) {
        List<PageRoleDisplay> pageRoles = repository.findAll();
        model.addAttribute("pageRoles", pageRoles);
    }

}
