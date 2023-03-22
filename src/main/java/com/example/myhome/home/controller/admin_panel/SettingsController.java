package com.example.myhome.home.controller.admin_panel;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SettingsController {

    @GetMapping("/admin/services")
    public String showEditHomePage(Model model) {return "settings_services";}

    @GetMapping("/admin/tariffs")
    public String showEditAboutPage(Model model) {return "settings_tariffs";}

    @GetMapping("/admin/users")
    public String showEditServicesPage(Model model) {return "settings_users";}

    @GetMapping("/admin/payment-details")
    public String showEditTariffsPage(Model model) {return "settings_payment";}

    @GetMapping("/admin/income-expense")
    public String showEditContactsPage(Model model) {return "settings_inc_exp";}

}
