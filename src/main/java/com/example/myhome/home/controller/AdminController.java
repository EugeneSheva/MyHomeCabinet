package com.example.myhome.home.controller;

import com.example.myhome.home.dto.AdminDTO;
import com.example.myhome.home.model.Admin;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.service.AdminService;
import com.example.myhome.util.MappingUtils;
import com.example.myhome.util.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired private AuthenticationManager authenticationManager;

    @GetMapping
    public String showAdminsPage(Model model,
                                 FilterForm form) throws IllegalAccessException {

        Page<AdminDTO> adminPage;
        Pageable pageable = PageRequest.of((form.getPage() == null) ? 0 : form.getPage()-1 ,15);

        adminPage = adminService.findAllByFiltersAndPage(form, pageable);

        model.addAttribute("admins", adminPage);
        model.addAttribute("filter_form", form);

        return "admin_panel/system_settings/settings_users";
    }

    @GetMapping("/{id}")
    public String showAdminProfile(@PathVariable long id, Model model) {
        AdminDTO admin = MappingUtils.fromAdminToDTO(adminService.findAdminById(id));
        model.addAttribute("admin", admin);
        return "admin_panel/system_settings/admin_profile";
    }

    @GetMapping("/create")
    public String showCreateAdminPage(Model model) {
        model.addAttribute("adminDTO", new AdminDTO());
        return "admin_panel/system_settings/admin_card";
    }

    @GetMapping("/update/{id}")
    public String showUpdateAdminPage(@PathVariable long id, Model model) {
        AdminDTO admin = MappingUtils.fromAdminToDTO(adminService.findAdminById(id));
        model.addAttribute("adminDTO", admin);
        return "admin_panel/system_settings/admin_card";
    }

    @PostMapping("/create")
    public String createAdmin(@Valid @ModelAttribute AdminDTO dto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) return "admin_panel/system_settings/admin_card";
        else {
            Admin admin = MappingUtils.fromDTOToAdmin(dto);
            adminService.saveAdmin(admin);
            return "redirect:/admin/admins";
        }
    }

    @PostMapping("/update/{id}")
    public String updateAdmin(@PathVariable long id, @Valid @ModelAttribute AdminDTO dto, BindingResult bindingResult) {
        dto.setId(id);

        if(bindingResult.hasErrors()) return "admin_panel/system_settings/admin_card";
        else {
            Admin admin = MappingUtils.fromDTOToAdmin(dto);
            adminService.saveAdmin(admin);



            return "redirect:/admin/admins";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable long id) {
        adminService.deleteAdminById(id);
        return "redirect:/admin/admins";
    }

    @GetMapping("/invite/{id}")
    public @ResponseBody String inviteAdmin(@PathVariable long id) {
        return "User with ID " + id + " - invited";
    }

    // =========

//    @GetMapping("/get-masters-by-type")
//    public @ResponseBody List<Admin> getMastersByType(@RequestParam String type) {
//        if(type.equalsIgnoreCase(UserRole.ROLE_ANY.name()))
//            return adminService.findAll().stream()
//                    .filter(admin -> admin.getRole() != UserRole.ROLE_ADMIN && admin.getRole() != UserRole.ROLE_DIRECTOR
//                    && admin.getRole() != UserRole.ROLE_MANAGER).collect(Collectors.toList());
//        else return adminService.getAdminsByRole(UserRole.valueOf(type.toUpperCase()));
//    }

    @GetMapping("/get-all-masters")
    public @ResponseBody Map<String, Object> getAllMasters(@RequestParam String search, @RequestParam int page) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Boolean> pagination = new HashMap<>();
        pagination.put("more", (page*5L) < adminService.countAllMasters());
        map.put("results", adminService.findAllMasters(search, page-1));
        map.put("pagination", pagination);
        System.out.println(map.get("results").toString());
        System.out.println(map.get("pagination").toString());
        return map;
    }

}
