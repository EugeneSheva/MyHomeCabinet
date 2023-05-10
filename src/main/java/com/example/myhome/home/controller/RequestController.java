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
import java.time.format.DateTimeFormatter;
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

        model.addAttribute("owners", ownerService.findAllDTO());
        model.addAttribute("masters", adminService.findAllMasters());
        model.addAttribute("master_types", adminService.getMasterRoles());
        model.addAttribute("filter_form", filterForm);

        return "admin_panel/requests/requests";
    }

    @GetMapping("/create")
    public String showCreateRequestPage(Model model) {

        RepairRequestDTO repairRequestDTO = new RepairRequestDTO();
        repairRequestDTO.setRequest_time(LocalTime.now());
        repairRequestDTO.setRequest_date(LocalDate.now());

        model.addAttribute("repairRequestDTO", repairRequestDTO);
        model.addAttribute("id", repairRequestService.getMaxId()+1);
        model.addAttribute("owners", ownerService.findAllDTO());
        model.addAttribute("masters", adminService.findAllMasters());
        model.addAttribute("master_types", adminService.getMasterRoles());

        model.addAttribute("date", LocalDate.now());
        model.addAttribute("time", LocalTime.now());

        return "admin_panel/requests/request_card";
    }

    @GetMapping("/update/{id}")
    public String showUpdateRequestPage(@PathVariable long id, Model model) {
        RepairRequestDTO request = repairRequestService.findRequestDTOById(id);
        if(request.getBest_time() == null) request.setBest_time(LocalDateTime.now().toString());
        log.info(request.toString());
        model.addAttribute("repairRequestDTO", request);
        model.addAttribute("id", id);
        model.addAttribute("date", request.getRequest_date());
        model.addAttribute("time", request.getRequest_time());
        model.addAttribute("best_date", LocalDateTime.parse(request.getBest_time(), DateTimeFormatter.ofPattern("yyyy-MM-dd - HH:mm")).toLocalDate());
        model.addAttribute("best_time", LocalDateTime.parse(request.getBest_time(), DateTimeFormatter.ofPattern("yyyy-MM-dd - HH:mm")).toLocalTime());
        model.addAttribute("owners", ownerService.findAll());
        model.addAttribute("masters", adminService.findAllMasters());
        model.addAttribute("master_types", adminService.getMasterRoles());
        model.addAttribute("owner_apartments", ownerService.findOwnerApartments(request.getOwnerID()));

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
    public String createRequest(@ModelAttribute RepairRequestDTO repairRequestDTO,
                                BindingResult bindingResult,
//                                @RequestParam(required = false) String date,
//                                @RequestParam(required = false) String time,
                                @RequestParam(required = false) String best_date,
                                @RequestParam(required = false) String best_time,
                                Model model) {

        if(best_date == null || best_date.isEmpty()) best_date = LocalDate.now().plusDays(2).toString();
        if(best_time == null || best_time.isEmpty()) best_time = LocalTime.of(12, 0).toString();
        if(repairRequestDTO.getMasterTypeID() == null || repairRequestDTO.getMasterTypeID() < 0) repairRequestDTO.setMasterTypeID(null);

//        repairRequestDTO.setRequest_date(LocalDate.parse(date));
//        repairRequestDTO.setRequest_time(LocalTime.parse(time));
        repairRequestDTO.setBest_time(best_date + " - " + best_time);

        validator.validate(repairRequestDTO, bindingResult);
        log.info(bindingResult.getAllErrors().toString());
        if(bindingResult.hasErrors()) {
            model.addAttribute("masters", adminService.findAllMasters());
            model.addAttribute("master_types", adminService.getMasterRoles());
            return "admin_panel/requests/request_card";
        }

        repairRequestService.saveRequest(repairRequestDTO);
        return "redirect:/admin/requests";
    }

    @PostMapping("/update/{id}")
    public String updateRequest(@PathVariable long id,
                                @ModelAttribute RepairRequestDTO repairRequestDTO,
                                BindingResult bindingResult,
                                @RequestParam(required = false) String best_date,
                                @RequestParam(required = false) String best_time,
                                Model model) {

        if(best_date == null) best_date = LocalDate.now().plusDays(2).toString();
        if(best_time == null) best_time = LocalTime.of(12, 0).toString();

        repairRequestDTO.setBest_time(best_date + " - " + best_time);

        validator.validate(repairRequestDTO, bindingResult);
        log.info(bindingResult.getAllErrors().toString());
        if(bindingResult.hasErrors()) {
            model.addAttribute("masters", adminService.findAllMasters());
            model.addAttribute("master_types", adminService.getMasterRoles());
            return "admin_panel/requests/request_card";
        }

        repairRequestService.saveRequest(repairRequestDTO);
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
