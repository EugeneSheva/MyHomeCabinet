package com.example.myhome.home.controller;

import com.example.myhome.home.configuration.security.CustomAdminDetails;
import com.example.myhome.home.configuration.security.CustomUserDetails;
import com.example.myhome.home.model.Admin;
import com.example.myhome.home.model.Owner;
import com.example.myhome.home.model.PageRoleDisplay;
import com.example.myhome.home.repository.PageRoleDisplayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private PageRoleDisplayRepository repository;

    @ModelAttribute
    public void addPagePermissions(Model model) {
        List<PageRoleDisplay> pageRoles = repository.findAll();
        model.addAttribute("pageRoles", pageRoles);
    }

    @ModelAttribute
    public void addLoggedInAdmin(HttpSession session, Model model) {
        CustomAdminDetails details = (CustomAdminDetails) session.getAttribute("admin");
        Admin admin = (details != null) ? details.getAdmin() : null;
        if(admin != null) model.addAttribute("auth_admin", admin);
    }

    @ModelAttribute
    public void addLoggedInUser(HttpSession session, Model model) {
        CustomUserDetails details = (CustomUserDetails) session.getAttribute("user");
        Owner owner = (details != null) ? details.getOwner() : null;
        if(owner != null) model.addAttribute("auth_owner", owner);
    }

}
