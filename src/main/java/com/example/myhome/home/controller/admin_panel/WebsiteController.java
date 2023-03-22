package com.example.myhome.home.controller.admin_panel;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/website")
public class WebsiteController {

    @GetMapping("/home")
    public String showEditHomePage(Model model) {return "website_home";}

    @GetMapping("/about")
    public String showEditAboutPage(Model model) {return "website_about";}

    @GetMapping("/services")
    public String showEditServicesPage(Model model) {return "website_services";}

    @GetMapping("/tariffs")
    public String showEditTariffsPage(Model model) {return "website_tariffs";}

    @GetMapping("/contacts")
    public String showEditContactsPage(Model model) {return "website_contacts";}

}
