package com.example.myhome.home.controller;

import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.model.Owner;
import com.example.myhome.home.model.OwnerDTO;
import com.example.myhome.home.service.OwnerService;
import com.example.myhome.home.validator.BuildingValidator;
import com.example.myhome.home.validator.OwnerValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/owners")
@Log
public class OwnerController {

    @Value("${upload.path}")
    private String uploadPath;
    private final OwnerService ownerService;
    private final OwnerValidator ownerValidator;

    @GetMapping("/")
    public String getOwners(Model model) {
        List<Owner> ownerList = ownerService.findAll();
        model.addAttribute("owners", ownerList);
        return "admin_panel/owners/owners";
    }

    @GetMapping("/{id}")
    public String getOwner(@PathVariable("id") Long id, Model model) {
        Owner owner = ownerService.findById(id);
        model.addAttribute("owner", owner);
        return "admin_panel/owners/owner";
    }

    @GetMapping("/new")
    public String createOwner(Model model) {
        Owner owner = new Owner();
        model.addAttribute("owner", owner);
        return "admin_panel/owners/owner_edit";
    }
    @GetMapping("edit/{id}")
    public String editeOwner(@PathVariable("id") Long id, Model model) {
        Owner owner = ownerService.findById(id);
        model.addAttribute("owner", owner);
        return "admin_panel/owners/owner_edit";
    }

    @PostMapping("/save")
    public String saveCoffee(@Valid @ModelAttribute("owner") Owner owner, BindingResult bindingResult, @RequestParam("img1") MultipartFile file) throws IOException {
        ownerValidator.validate(owner, bindingResult);
        if (bindingResult.hasErrors()) {
            return "admin_panel/owners/owner_edit";
        } else {
            owner.setProfile_picture(ownerService.saveOwnerImage(owner.getId(), file));
            ownerService.save(owner);
        }
        return "redirect:/admin/owners/";
    }

    @GetMapping("/delete/{id}")
    public String dellete(@PathVariable("id") Long id) {
        Owner owner = ownerService.findById(id);
        try {
            Files.deleteIfExists(Path.of(uploadPath + owner.getProfile_picture()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ownerService.deleteById(id);
        return "redirect:/admin/owners/";
    }

    //Получить квартиры какого-то владельца
    @GetMapping("/get-apartments/{id}")
    public @ResponseBody List<Apartment> getOwnerApartments(@PathVariable long id) {
        return ownerService.findById(id).getApartments();
    }

    @GetMapping("/get-apartment-accounts")
    public @ResponseBody List<Long> getOwnerApartmentAccountsIds(@RequestParam long owner_id) {
        return ownerService.getOwnerApartmentAccountsIds(owner_id);
    }

    @GetMapping("/get-all-owners")
    public @ResponseBody Map<String, Object> getAllOwners(@RequestParam String search, @RequestParam int page) {
        log.info(ownerService.findAllDTO().toString());
        log.info("Getting all owners that have in their name: " + search);
        Map<String, Object> map = new HashMap<>();
        Map<String, Boolean> pagination = new HashMap<>();
        pagination.put("more", (page*10L) < ownerService.countAllOwners());
        map.put("results", ownerService.getOwnerDTOByPage(search, page-1));
        map.put("pagination", pagination);
        System.out.println(map.get("results").toString());
        System.out.println(map.get("pagination").toString());
        return map;
    }
}
