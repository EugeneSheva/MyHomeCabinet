package com.example.myhome.home.controller;

import com.example.myhome.home.model.Admin;
import com.example.myhome.home.repository.AdminRepository;
import com.example.myhome.util.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/admins")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping
    public String showAdminsPage(Model model) {
        model.addAttribute("admins", adminRepository.findAll());
        return "admin_panel/system_settings/settings_users";
    }

    @GetMapping("/{id}")
    public String showAdminProfile(@PathVariable long id, Model model) {
        model.addAttribute("admin", adminRepository.findById(id).orElseGet(Admin::new));
        return "admin_panel/system_settings/admin_profile";
    }

    @GetMapping("/create")
    public String showCreateAdminPage(Model model) {
        model.addAttribute("admin", new Admin());
        return "admin_panel/system_settings/admin_card";
    }

    @GetMapping("/update/{id}")
    public String showUpdateAdminPage(@PathVariable long id, Model model) {
        model.addAttribute("admin", adminRepository.findById(id).orElseGet(Admin::new));
        return "admin_panel/system_settings/admin_card";
    }

    @PostMapping("/create")
    public String createAdmin(@ModelAttribute Admin admin) {
        adminRepository.save(admin);
        return "redirect:/admin/admins";
    }

    @PostMapping("/update/{id}")
    public String updateAdmin(@PathVariable long id, @ModelAttribute Admin admin) {
        admin.setId(id);
        adminRepository.save(admin);
        return "redirect:/admin/admins";
    }

    @GetMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable long id) {
        adminRepository.deleteById(id);
        return "redirect:/admin/admins";
    }

    @GetMapping("/invite/{id}")
    public void inviteAdmin(@PathVariable long id) {}

    // =========

    @GetMapping("/get-masters-by-type")
    public @ResponseBody List<Admin> getMastersByType(@RequestParam String type) {
        System.out.println(UserRole.ANY.name());
        if(type.equalsIgnoreCase(UserRole.ANY.name()))
            return adminRepository.findAll().stream()
                    .filter(admin -> admin.getRole() != UserRole.ADMIN && admin.getRole() != UserRole.DIRECTOR
                    && admin.getRole() != UserRole.MANAGER).collect(Collectors.toList());
        else return adminRepository.getAdminsByRole(UserRole.valueOf(type.toUpperCase()));
    }

}
