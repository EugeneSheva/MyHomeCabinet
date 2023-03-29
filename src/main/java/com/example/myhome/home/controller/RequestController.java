package com.example.myhome.home.controller;

import com.example.myhome.home.model.Admin;
import com.example.myhome.home.model.RepairRequest;
import com.example.myhome.home.repos.AdminRepository;
import com.example.myhome.home.repos.OwnerRepository;
import com.example.myhome.home.repos.RepairRequestRepository;
import com.example.myhome.util.UserRole;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/requests")
@Log
public class RequestController {

    @Autowired
    private RepairRequestRepository repairRequestRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping
    public String showRequestsPage(Model model) {
        model.addAttribute("requests", repairRequestRepository.findAll());
        return "requests";
    }

    @GetMapping("/create")
    public String showCreateRequestPage(Model model) {

        model.addAttribute("request", new RepairRequest());
        model.addAttribute("date", LocalDate.now());
        model.addAttribute("time", LocalTime.now());
        model.addAttribute("owners", ownerRepository.findAll());
        model.addAttribute("masters", adminRepository.findAll().stream()
                .filter(master -> master.getRole() != UserRole.ADMIN &&
                        master.getRole() != UserRole.MANAGER &&
                        master.getRole() != UserRole.DIRECTOR)
                .sorted(Comparator.comparing(o -> o.getRole().getName()))
                .collect(Collectors.toList())
        );
        return "request_card";
    }

    @GetMapping("/update/{id}")
    public String showUpdateRequestPage(@PathVariable long id, Model model) {
        RepairRequest request = repairRequestRepository.findById(id).orElseThrow();
        log.info(request.toString());
        model.addAttribute("request", request);
        model.addAttribute("date", request.getRequest_date().toLocalDate());
        model.addAttribute("time", request.getRequest_date().toLocalTime());
        model.addAttribute("owners", ownerRepository.findAll());
        model.addAttribute("masters", adminRepository.findAll().stream()
                .filter(master -> master.getRole() != UserRole.ADMIN &&
                        master.getRole() != UserRole.MANAGER &&
                        master.getRole() != UserRole.DIRECTOR)
                .sorted(Comparator.comparing(o -> o.getRole().getName()))
                .collect(Collectors.toList())
        );
        return "request_card";
    }

    @GetMapping("/info/{id}")
    public String getRequestInfoPage(@PathVariable long id, Model model) {
        RepairRequest request = repairRequestRepository.findById(id).orElseThrow();
        model.addAttribute("request", request);
        model.addAttribute("request_date", request.getRequest_date().toLocalDate());
        model.addAttribute("request_time", request.getRequest_date().toLocalTime());
        return "request_profile";
    }

    @PostMapping("/create")
    public String createRequest(@ModelAttribute RepairRequest request,
                                @RequestParam String date,
                                @RequestParam String time) {
        request.setRequest_date(LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time)));
        repairRequestRepository.save(request);
        return "redirect:/admin/requests";
    }

    @PostMapping("/update/{id}")
    public String updateRequest(@PathVariable long id,
                                @ModelAttribute RepairRequest request) {
        RepairRequest originalRequest = repairRequestRepository.findById(id).orElseThrow();
        request.setRequest_date(originalRequest.getRequest_date());
        repairRequestRepository.save(request);
        return "redirect:/admin/requests";
    }

    @GetMapping("/delete/{id}")
    public String deleteRequest(@PathVariable long id) {
        repairRequestRepository.deleteById(id);
        return "redirect:/admin/requests";
    }



}
