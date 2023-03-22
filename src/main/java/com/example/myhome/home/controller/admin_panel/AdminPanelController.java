package com.example.myhome.home.controller.admin_panel;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminPanelController {

    @GetMapping("/messages")
    public String showMessagesPage(Model model) {return "messages";}

    @GetMapping("/requests")
    public String showRequestsPage(Model model) {return "requests";}

    @GetMapping("/meters")
    public String showMetersPage(Model model) {return "meters";}
}
