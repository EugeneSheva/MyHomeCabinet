package com.example.myhome.home.controller;

import com.example.myhome.home.configuration.security.CustomAdminDetails;
import com.example.myhome.home.configuration.security.CustomUserDetails;
import com.example.myhome.home.model.Admin;
import com.example.myhome.home.model.Owner;
import com.example.myhome.home.model.PageRoleDisplay;
import com.example.myhome.home.repository.PageRoleDisplayRepository;
import com.example.myhome.home.service.AdminService;
import com.example.myhome.home.service.OwnerService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@ControllerAdvice
@Log
public class GlobalControllerAdvice {

    @Autowired
    private PageRoleDisplayRepository repository;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private AdminService adminService;

    @ModelAttribute
    public void addPagePermissions(Model model) {
        List<PageRoleDisplay> pageRoles = repository.findAll();
        model.addAttribute("pageRoles", pageRoles);
    }

    @ModelAttribute
    public void addLoggedInOwnerAttribute(HttpSession session,
                                          HttpServletRequest request, HttpServletResponse response,
                                          Model model) {

        // без этих двух строчек почему-то кидает NPE , даже когда весь сайт работает

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null) return;

        // без этих двух строчек почему-то кидает NPE , даже когда весь сайт работает

        Object principal = auth.getPrincipal();
        if(principal instanceof CustomUserDetails) {
            CustomUserDetails details = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Owner owner = ownerService.fromCustomUserDetailsToOwner(details);
            model.addAttribute("auth_owner", owner);
        }
        else {
            model.addAttribute("auth_owner", new Owner());
        }

    }

    @ModelAttribute
    public void addLoggedInAdminAttribute(HttpSession session,
                                          HttpServletRequest request, HttpServletResponse response,
                                          Model model) {

        // без этих двух строчек почему-то кидает NPE , даже когда весь сайт работает

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null) return;

        // без этих двух строчек почему-то кидает NPE , даже когда весь сайт работает

        Object principal = auth.getPrincipal();
        if(principal instanceof CustomAdminDetails) {
            CustomAdminDetails details = (CustomAdminDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Admin admin = adminService.fromCustomAdminDetailsToAdmin(details);
            model.addAttribute("auth_admin", admin);
        }
        else {
            model.addAttribute("auth_admin", new Admin());
        }

    }

}
