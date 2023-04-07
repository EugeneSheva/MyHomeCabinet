package com.example.myhome.home.controller;

import com.example.myhome.home.model.RepairRequest;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.AdminRepository;
import com.example.myhome.home.repository.OwnerRepository;
import com.example.myhome.home.repository.RepairRequestRepository;
import com.example.myhome.home.service.AdminService;
import com.example.myhome.home.service.OwnerService;
import com.example.myhome.home.service.RepairRequestService;
import com.example.myhome.util.UserRole;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/requests")
@Log
public class RequestController {

    @Autowired
    private RepairRequestService repairRequestService;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private AdminService adminService;

    @GetMapping
    public String showRequestsPage(Model model,
                                   FilterForm filterForm) throws IllegalAccessException {

        List<RepairRequest> requestList;

        if(!filterForm.filtersPresent()) requestList = repairRequestService.findAllRequests();
        else requestList = repairRequestService.findAllBySpecification(filterForm);

        model.addAttribute("requests", requestList);
        model.addAttribute("owners", ownerService.findAllDTO());
        model.addAttribute("masters", adminService.findAllDTO());

        log.info(filterForm.toString());

        model.addAttribute("filter_form", filterForm);
        return "admin_panel/requests/requests";
    }

    @GetMapping("/create")
    public String showCreateRequestPage(Model model) {

        model.addAttribute("request", new RepairRequest());
        model.addAttribute("date", LocalDate.now());
        model.addAttribute("time", LocalTime.now());
        model.addAttribute("owners", ownerService.findAll());
        model.addAttribute("masters", adminService.findAll().stream()
                .filter(master -> master.getRole() != UserRole.ADMIN &&
                        master.getRole() != UserRole.MANAGER &&
                        master.getRole() != UserRole.DIRECTOR)
                .sorted(Comparator.comparing(o -> o.getRole().getName()))
                .collect(Collectors.toList())
        );
        return "admin_panel/requests/request_card";
    }

    @GetMapping("/update/{id}")
    public String showUpdateRequestPage(@PathVariable long id, Model model) {
        RepairRequest request = repairRequestService.findRequestById(id);
        if(request.getBest_time_request() == null) request.setBest_time_request(LocalDateTime.now());
        log.info(request.toString());
        model.addAttribute("request", request);
        model.addAttribute("date", request.getRequest_date().toLocalDate());
        model.addAttribute("time", request.getRequest_date().toLocalTime());
        model.addAttribute("best_date", request.getBest_time_request().toLocalDate());
        model.addAttribute("best_time", request.getBest_time_request().toLocalTime());
        model.addAttribute("owners", ownerService.findAll());
        model.addAttribute("masters", adminService.findAll().stream()
                .filter(master -> master.getRole() != UserRole.ADMIN &&
                        master.getRole() != UserRole.MANAGER &&
                        master.getRole() != UserRole.DIRECTOR)
                .sorted(Comparator.comparing(o -> o.getRole().getName()))
                .collect(Collectors.toList())
        );
        return "admin_panel/requests/request_card";
    }

    @GetMapping("/info/{id}")
    public String getRequestInfoPage(@PathVariable long id, Model model) {
        RepairRequest request = repairRequestService.findRequestById(id);
        model.addAttribute("request", request);
        model.addAttribute("request_date", request.getRequest_date().toLocalDate());
        model.addAttribute("request_time", request.getRequest_date().toLocalTime());
        return "admin_panel/requests/request_profile";
    }

    @PostMapping("/create")
    public String createRequest(@ModelAttribute RepairRequest request,
                                BindingResult bindingResult,
                                @RequestParam String date,
                                @RequestParam String time,
                                @RequestParam(required = false) String best_date,
                                @RequestParam(required = false) String best_time) {
        if(bindingResult.hasErrors()) log.info(bindingResult.getAllErrors().toString());
        request.setRequest_date(LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time)));
        request.setBest_time_request(LocalDateTime.of(LocalDate.parse(best_date), LocalTime.parse(best_time)));
        repairRequestService.saveRequest(request);
        return "redirect:/admin/requests";
    }

    @PostMapping("/update/{id}")
    public String updateRequest(@PathVariable long id,
                                @ModelAttribute RepairRequest request,
                                @RequestParam(required = false) String best_date,
                                @RequestParam(required = false) String best_time) {
        RepairRequest originalRequest = repairRequestService.findRequestById(id);
        request.setRequest_date(originalRequest.getRequest_date());
        request.setBest_time_request(LocalDateTime.of(LocalDate.parse(best_date), LocalTime.parse(best_time)));
        repairRequestService.saveRequest(request);
        return "redirect:/admin/requests";
    }

    @GetMapping("/delete/{id}")
    public String deleteRequest(@PathVariable long id) {
        repairRequestService.deleteRequestById(id);
        return "redirect:/admin/requests";
    }



}
