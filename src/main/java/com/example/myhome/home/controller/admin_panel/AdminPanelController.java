package com.example.myhome.home.controller.admin_panel;

import com.example.myhome.home.model.*;
import com.example.myhome.home.repos.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private BuildingSectionRepository buildingSectionRepository;

    @GetMapping("/messages")
    public String showMessagesPage(Model model) {return "messages";}

    @GetMapping("/messages/create")
    public String showCreateMessagePage(Model model) {
        model.addAttribute("message", new Message());
        return "message_card";
    }

    @GetMapping("/requests")
    public String showRequestsPage(Model model) {return "requests";}

    @GetMapping("/requests/create")
    public String showCreateRequestPage(Model model) {
        model.addAttribute("request", new RepairRequest());
        return "request_card";
    }

    @GetMapping("/requests/update/{id}")
    public String showUpdateRequestPage(@PathVariable long id, Model model) {
        model.addAttribute("request", repairRequestRepository.findById(id).orElseGet(RepairRequest::new));
        return "request_card";
    }

    @GetMapping("/meters")
    public String showMetersPage(Model model) {
        model.addAttribute("now", LocalDate.now());
        return "meters";
    }

    @GetMapping("/meters/create")
    public String showCreateMeterPage(Model model) {
        //Добавить дома, потом их секции, потом квартиры
        //model.addAttribute("buildings", );
        long newId = 0L;
        Optional<MeterData> meterWithBiggestId = meterDataRepository.findFirstByOrderByIdDesc();
        if(meterWithBiggestId.isEmpty()) newId = 1L;
        else newId = meterWithBiggestId.get().getId()+1;
        log.info("new id for meter data: " + newId);
        model.addAttribute("id",newId);
        model.addAttribute("meter", new MeterData());
        model.addAttribute("services", serviceRepository.findAll());
        model.addAttribute("buildings", buildingRepository.findAll());
        model.addAttribute("now", LocalDate.now());
        return "meter_card";
    }

    @GetMapping("/meters/update/{id}")
    public String showUpdateMeterPage(@PathVariable long id, Model model) {
        model.addAttribute("meter", meterDataRepository.findById(id).orElseGet(MeterData::new));
        model.addAttribute("now", LocalDate.now());
        return "meter_card";
    }

    @GetMapping("/meters/data")
    public String showSingleMeterDataPage(@RequestParam long id, Model model) {
        model.addAttribute("meter", meterDataRepository.findById(id).orElseGet(MeterData::new));
        return "meter_card";
    }

    @GetMapping("/buildings/get-sections/{id}")
    public @ResponseBody List<BuildingSection> getBuildingSections(@PathVariable long id) {
        return buildingRepository.findById(id).orElseThrow().getSections();
    }

    @GetMapping("/buildings/get-section-apartments/{id}")
    public @ResponseBody List<Apartment> getBuildingSectionApartments(@PathVariable long id) {
        return buildingSectionRepository.findById(id).orElseThrow().getApartments();
    }
}
