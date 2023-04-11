package com.example.myhome.home.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping("/cabinet/site/login")
    public String showLoginPage(Model model) {return "main_website/login";}

    @GetMapping("/admin/site/login")
    public String showAdminLoginPage(Model model) {return "main_website/admin_login";}

    @GetMapping("/cabinet/site/register")
    public String showRegisterPage(Model model) {return "main_website/register";}

    @PostMapping("/cabinet/site/register")
    public String registerUser() {return "main_website/register";}

    @GetMapping("/cabinet/logout")
    public String logout () {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/cabinet/site/login";
    }

    @GetMapping("/admin/logout")
    public String adminLogout () {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/admin/site/login";
    }

}
