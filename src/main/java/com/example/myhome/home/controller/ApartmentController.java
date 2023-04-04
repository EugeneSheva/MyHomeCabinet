package com.example.myhome.home.controller;

import com.example.myhome.home.model.*;
import com.example.myhome.home.service.ApartmentService;
import com.example.myhome.home.service.BuildingService;
import com.example.myhome.home.service.OwnerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/apartments")
public class ApartmentController {

    @Value("${upload.path}")
    private String uploadPath;
    private final ApartmentService apartmentService;
    private final BuildingService buildingService;
    private final OwnerService ownerService;


    @GetMapping("/")
    public String getApartment(Model model) {
        List<Apartment> apartmentList = apartmentService.findAll();
        model.addAttribute("apartments", apartmentList);
        return "admin_panel/apartments/apartments";
    }

    @GetMapping("/{id}")
    public String getApartment(@PathVariable("id") Long id, Model model) {
        Apartment apartment = apartmentService.findById(id);
        model.addAttribute("apartment", apartment);
        return "admin_panel/apartments/apartment";
    }

    @GetMapping("/new")
    public String createApartment(Model model) throws JsonProcessingException {
        List<OwnerDTO> ownerDTOList = ownerService.findAllDTO();
        model.addAttribute("owners", ownerDTOList);
        Apartment apartment = new Apartment();
        model.addAttribute("apartment", apartment);
        List<BuildingDTO> buildingList = buildingService.findAllDTO();
        model.addAttribute("buildings", buildingList);
        return "admin_panel/apartments/apartment_edit";
    }
    @GetMapping("edit/{id}")
    public String editApartment(@PathVariable("id") Long id, Model model) {
        List<OwnerDTO> ownerDTOList = ownerService.findAllDTO();
        model.addAttribute("owners", ownerDTOList);
        Apartment apartment = apartmentService.findById(id);
        model.addAttribute("apartment", apartment);
        List<BuildingDTO> buildingList = buildingService.findAllDTO();
        model.addAttribute("buildings", buildingList);
        return "admin_panel/apartments/apartment_edit";
    }

    @PostMapping("/save")
    public String saveCoffee(@ModelAttribute("apartment") Apartment apartment) throws IOException {
//        Apartment building = buildingService.saveBuildindImages(id, file1, file2, file3, file4, file5);
        apartmentService.save(apartment);
        return "redirect:/admin/apartments/";
    }

    @GetMapping("/delete/{id}")
    public String dellete(@PathVariable("id") Long id) {
        apartmentService.deleteById(id);
        return "redirect:/admin/apartments/";
    }

    @GetMapping("/get-owner")
    public @ResponseBody Owner getOwner(@RequestParam long flat_id) {
        return apartmentService.findById(flat_id).getOwner();
    }

    @GetMapping("/get-meters")
    public @ResponseBody List<MeterData> getMeters(@RequestParam long flat_id) {
        return apartmentService.findById(flat_id).getMeterDataList();
    }


}
