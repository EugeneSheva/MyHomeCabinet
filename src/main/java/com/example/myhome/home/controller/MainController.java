package com.example.myhome.home.controller;

import com.example.myhome.home.repos.PageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    @Autowired
    private PageRepository pageRepository;

    @GetMapping("/")
    public String showMainPage(Model model) {
        model.addAttribute("page", pageRepository.getMainPage());
        return "main_website/index";
    }

    @GetMapping("/about")
    public String showAboutPage(Model model) {
        model.addAttribute("page", pageRepository.getAboutPage());
        return "main_website/about";
    }

    @GetMapping("/services")
    public String showServicesPage(Model model) {
        model.addAttribute("page", pageRepository.getServicesPage());
        return "main_website/services";
    }

    @GetMapping("/contacts")
    public String showContactsPage(Model model) {
        model.addAttribute("page", pageRepository.getContactsPage());
        return "main_website/contacts";
    }

}
