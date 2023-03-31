package com.example.myhome.home.controller.admin_panel;

import com.example.myhome.home.model.*;
import com.example.myhome.home.repository.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@Log
public class AdminPanelController {

    @Autowired
    private MeterDataRepository meterDataRepository;

    @Autowired
    private RepairRequestRepository repairRequestRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private BuildingRepository buildingRepository;


    @GetMapping("/messages")
    public String showMessagesPage(Model model) {return "messages";}

    @GetMapping("/messages/create")
    public String showCreateMessagePage(Model model) {
        model.addAttribute("message", new Message());
        return "message_card";
    }

    @GetMapping("/buildings/get-sections/{id}")
    public @ResponseBody List<String> getBuildingSections(@PathVariable long id) {
        return buildingRepository.findById(id).orElseThrow().getSections();
    }

    @GetMapping("/buildings/get-section-apartments")
    public @ResponseBody List<Apartment> getBuildingSectionApartments(@RequestParam long id, @RequestParam String section_name) {

        List<Apartment> apartments = buildingRepository.findById(id).orElseThrow().getApartments();
        return apartments.stream().filter((apartment) -> apartment.getSection().equals(section_name)).collect(Collectors.toList());

    }
    //через @Query
    @GetMapping("/buildings/get-section-apartments/{id}")
    public @ResponseBody List<Apartment> getBuildingSectionApartmentsFromQuery(@PathVariable long id, @RequestParam String section_name) {
        return buildingRepository.getSectionApartments(id, section_name);
    }
}
