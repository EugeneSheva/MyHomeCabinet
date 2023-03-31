package com.example.myhome.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/cabinet/site/login")
    public String showLoginPage(Model model) {return "main_website/login";}

    @GetMapping("/admin/site/login")
    public String showAdminLoginPage(Model model) {return "main_website/admin_login";}
}
