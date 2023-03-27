package com.example.myhome.home.controller;

import com.example.myhome.home.model.Owner;
import com.example.myhome.home.services.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/owners")
public class OwnerController {

    @Value("${upload.path}")
    private String uploadPath;
    private final OwnerService ownerService;

    @GetMapping("/")
    public String getOwners(Model model) {
        List<Owner> ownerList = ownerService.findAll();
        model.addAttribute("owners", ownerList);
        return "admin_panel/owners";
    }

    @GetMapping("/{id}")
    public String getOwner(@PathVariable("id") Long id, Model model) {
        Owner owner = ownerService.findById(id);
        model.addAttribute("owner", owner);
        return "admin_panel/owner";
    }

    @GetMapping("/new")
    public String createOwner(Model model) {
        Owner owner = new Owner();
        model.addAttribute("owner", owner);
        return "admin_panel/owner_edit";
    }
    @GetMapping("edit/{id}")
    public String editeOwner(@PathVariable("id") Long id, Model model) {
        Owner owner = ownerService.findById(id);
        model.addAttribute("owner", owner);
        return "admin_panel/owner_edit";
    }

    @PostMapping("/save")
    public String saveCoffee(@ModelAttribute("owner") Owner owner) throws IOException {
        ownerService.save(owner);
        return "redirect:/owners/";
    }

//    @GetMapping("/delete/{id}")
//    public String dellete(@PathVariable("id") Long id) {
//        Building building = buildingService.findById(id);
//        try {
//            Files.deleteIfExists(Path.of(uploadPath + building.getImg1()));
//            Files.deleteIfExists(Path.of(uploadPath + building.getImg2()));
//            Files.deleteIfExists(Path.of(uploadPath + building.getImg3()));
//            Files.deleteIfExists(Path.of(uploadPath + building.getImg4()));
//            Files.deleteIfExists(Path.of(uploadPath + building.getImg5()));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        buildingService.deleteById(id);
//        return "redirect:/buildings/";
//    }
}