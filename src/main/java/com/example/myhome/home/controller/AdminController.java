package com.example.myhome.home.controller;

import com.example.myhome.home.dto.AdminDTO;

import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.service.AdminService;
import com.example.myhome.home.service.impl.AdminServiceImpl;
import com.example.myhome.home.validator.AdminValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/admin/admins")
@RequiredArgsConstructor
@Log
public class AdminController {
    private final AdminServiceImpl adminServiceImpl;
    private final AdminService adminService;

    private final AdminValidator validator;


    // показать страничку с таблицей всех пользователей
    @GetMapping
    public String showAdminsPage(Model model,
                                 FilterForm form) throws IllegalAccessException {

        Page<AdminDTO> adminPage;
        Pageable pageable = PageRequest.of((form.getPage() == null) ? 0 : form.getPage()-1 ,15);

        adminPage = adminService.findAllByFiltersAndPage(form, pageable);
        model.addAttribute("totalPagesCount", adminPage.getTotalPages());
        model.addAttribute("admins", adminPage);
        model.addAttribute("filter_form", form);

        return "admin_panel/system_settings/settings_users";
    }

    // показать профиль конкретного пользователя
    @GetMapping("/{id}")
    public String showAdminProfile(@PathVariable long id, Model model) {
        AdminDTO admin = adminService.findAdminDTOById(id);
        model.addAttribute("admin", admin);
        return "admin_panel/system_settings/admin_profile";
    }

    // открыть страничку создания пользователя
    @GetMapping("/create")
    public String showCreateAdminPage(Model model) {
        model.addAttribute("adminDTO", new AdminDTO());
        model.addAttribute("roles", adminService.getAllRoles());
        return "admin_panel/system_settings/admin_card";
    }

    // открыть страничку обновления пользователя
    @GetMapping("/update/{id}")
    public String showUpdateAdminPage(@PathVariable long id, Model model) {
        AdminDTO admin = adminService.findAdminDTOById(id);
        model.addAttribute("adminDTO", admin);
        model.addAttribute("roles", adminService.getAllRoles());
        return "admin_panel/system_settings/admin_card";
    }

    // сохранить созданного пользователя
    @PostMapping("/create")
    public String createAdmin(@ModelAttribute AdminDTO dto, BindingResult bindingResult, Model model) {
        validator.validate(dto, bindingResult);
        if(bindingResult.hasErrors()) {
            model.addAttribute("roles", adminService.getAllRoles());
            return "admin_panel/system_settings/admin_card";
        }
        else {
            adminService.saveAdmin(dto);
            return "redirect:/admin/admins";
        }
    }

    // сохранить обновленного пользователя
    @PostMapping("/update/{id}")
    public String updateAdmin(@PathVariable long id, @ModelAttribute AdminDTO dto, BindingResult bindingResult, Model model) {
        dto.setId(id);
        validator.validate(dto, bindingResult);
        if(bindingResult.hasErrors()) {
            model.addAttribute("roles", adminService.getAllRoles());
            return "admin_panel/system_settings/admin_card";
        }
        else {
            adminService.saveAdmin(dto);
            return "redirect:/admin/admins";
        }
    }

    // удалить пользователя
    @GetMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable long id) {
        adminService.deleteAdminById(id);
        return "redirect:/admin/admins";
    }

    // отправить приглашение
    @GetMapping("/invite/{id}")
    public @ResponseBody String inviteAdmin(@PathVariable long id) {
        return "User with ID " + id + " - invited";
    }

    // =========

    // Получить всех специалистов: сантехник, электрик, ... (не управляющих) - для заявок вызова мастеров
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

    // Получить всех управляющих: директор, админ, бухгалтер... (не специалистов) - для кассы
    @GetMapping("/get-managers")
    public @ResponseBody Map<String, Object> getAllManagers(@RequestParam String search, @RequestParam int page) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Boolean> pagination = new HashMap<>();
        pagination.put("more", (page*5L) < adminService.countAllManagers());
        map.put("results", adminService.findAllMasters(search, page-1));
        map.put("pagination", pagination);
        System.out.println(map.get("results").toString());
        System.out.println(map.get("pagination").toString());
        return map;
    }
    @GetMapping("/get-admins")
    public @ResponseBody Page<AdminDTO> getAdmins(@RequestParam Integer page,
                                                  @RequestParam Integer size,
                                                  @RequestParam String filters) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FilterForm form = mapper.readValue(filters, FilterForm.class);
        return adminServiceImpl.findAllBySpecification(form, page, size);
    }

    // Получить мастеров/управляющих конкретного типа (ID пользовательской роли)
    @GetMapping("/get-masters-by-type")
    public @ResponseBody List<AdminDTO> getMastersByType(@RequestParam Long typeID) {
        return (typeID > 0) ? adminService.findMastersByType(typeID) : adminService.findAllMasters();
    }

}
