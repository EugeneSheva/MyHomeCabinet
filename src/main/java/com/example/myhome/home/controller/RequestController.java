package com.example.myhome.home.controller;

import com.example.myhome.home.model.RepairRequest;
import com.example.myhome.home.dto.RepairRequestDTO;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.service.impl.AdminServiceImpl;
import com.example.myhome.home.service.OwnerService;
import com.example.myhome.home.service.RepairRequestService;
import com.example.myhome.home.validator.RequestValidator;
import com.example.myhome.util.UserRole;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
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
    private AdminServiceImpl adminService;

    @Autowired private RequestValidator validator;

    @GetMapping
    public String showRequestsPage(Model model,
                                   FilterForm filterForm) throws IllegalAccessException {

        if(filterForm.getPage() == null) return "redirect:/admin/requests?page=1";

        Page<RepairRequest> requestList;

        requestList = repairRequestService.findAllBySpecification(filterForm);

        model.addAttribute("requests", requestList);
        model.addAttribute("totalPagesCount", requestList.getTotalPages());
        model.addAttribute("owners", ownerService.findAllDTO());
        model.addAttribute("masters", adminService.findAllDTO());

        log.info(filterForm.toString());

        model.addAttribute("filter_form", filterForm);
        model.addAttribute("page", filterForm.getPage());
        return "admin_panel/requests/requests";
    }

    @GetMapping("/create")
    public String showCreateRequestPage(Model model) {

        model.addAttribute("repairRequest", new RepairRequestDTO());
        model.addAttribute("id", repairRequestService.getMaxId()+1);
        model.addAttribute("owners", ownerService.findAllDTO());
        model.addAttribute("masters", adminService.findAllDTO());

        model.addAttribute("date", LocalDate.now());
        model.addAttribute("time", LocalTime.now());

        return "admin_panel/requests/request_card";
    }

    @GetMapping("/update/{id}")
    public String showUpdateRequestPage(@PathVariable long id, Model model) {
        RepairRequest repairRequest = repairRequestService.findRequestById(id);
        if(repairRequest.getBest_time_request() == null) repairRequest.setBest_time_request(LocalDateTime.now());
        log.info(repairRequest.toString());
        model.addAttribute("repairRequest", repairRequest);
        model.addAttribute("id", id);
        model.addAttribute("date", repairRequest.getRequest_date().toLocalDate());
        model.addAttribute("time", repairRequest.getRequest_date().toLocalTime());
        model.addAttribute("best_date", repairRequest.getBest_time_request().toLocalDate());
        model.addAttribute("best_time", repairRequest.getBest_time_request().toLocalTime());
        model.addAttribute("owners", ownerService.findAll());
        model.addAttribute("masters", adminService.findAll());

        return "admin_panel/requests/request_card";
    }

    @GetMapping("/info/{id}")
    public String getRequestInfoPage(@PathVariable long id, Model model) {
        RepairRequestDTO request = repairRequestService.findRequestDTOById(id);
        model.addAttribute("request", request);
        model.addAttribute("request_date", request.getRequest_date());
        model.addAttribute("request_time", request.getRequest_date());
        return "admin_panel/requests/request_profile";
    }

    @PostMapping("/create")
    public String createRequest(@ModelAttribute RepairRequest repairRequest,
                                BindingResult bindingResult,
                                @RequestParam(required = false) String date,
                                @RequestParam(required = false) String time,
                                @RequestParam(required = false) String best_date,
                                @RequestParam(required = false) String best_time) {

        if(best_date == null || best_date.isEmpty()) best_date = LocalDate.now().plusDays(2).toString();
        if(best_time == null || best_time.isEmpty()) best_time = LocalTime.of(12, 0).toString();

        repairRequest.setRequest_date(LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time)));
        repairRequest.setBest_time_request(LocalDateTime.of(LocalDate.parse(best_date), LocalTime.parse(best_time)));

        validator.validate(repairRequest, bindingResult);
        log.info(bindingResult.getAllErrors().toString());
        if(bindingResult.hasErrors()) return "admin_panel/requests/request_card";

        repairRequestService.saveRequest(repairRequest);
        return "redirect:/admin/requests";
    }

    @PostMapping("/update/{id}")
    public String updateRequest(@PathVariable long id,
                                @ModelAttribute RepairRequest repairRequest,
                                BindingResult bindingResult,
                                @RequestParam(required = false) String best_date,
                                @RequestParam(required = false) String best_time) {

        if(best_date == null) best_date = LocalDate.now().plusDays(2).toString();
        if(best_time == null) best_time = LocalTime.of(12, 0).toString();

        RepairRequest originalRequest = repairRequestService.findRequestById(id);
        repairRequest.setRequest_date(originalRequest.getRequest_date());
        repairRequest.setBest_time_request(LocalDateTime.of(LocalDate.parse(best_date), LocalTime.parse(best_time)));

        validator.validate(repairRequest, bindingResult);
        log.info(bindingResult.getAllErrors().toString());
        if(bindingResult.hasErrors()) return "admin_panel/requests/request_card";

        repairRequestService.saveRequest(repairRequest);
        return "redirect:/admin/requests";
    }

    @GetMapping("/delete/{id}")
    public String deleteRequest(@PathVariable long id) {
        repairRequestService.deleteRequestById(id);
        return "redirect:/admin/requests";
    }

    @GetMapping("/get-requests")
    public @ResponseBody Page<RepairRequestDTO> getRequests(@RequestParam Integer page,
                                                            @RequestParam Integer size,
                                                            @RequestParam String filters) throws JsonProcessingException, IllegalAccessException {
        ObjectMapper mapper = new ObjectMapper();
        FilterForm form = mapper.readValue(filters, FilterForm.class);
        return repairRequestService.findAllBySpecification(form, page, size);
    }

    @ModelAttribute
    public void addAttributes(Model model) {


    }



}
