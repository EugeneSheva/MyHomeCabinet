package com.example.myhome.home.controller;

import com.example.myhome.home.model.Service;
import com.example.myhome.home.model.ServiceForm;
import com.example.myhome.home.model.Unit;
import com.example.myhome.home.repository.ServiceRepository;
import com.example.myhome.home.repository.UnitRepository;
import com.example.myhome.home.service.ServiceService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/services")
@Log
public class ServiceController {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private ServiceService serviceService;

    @GetMapping
    public String showServicesPage(Model model){
        ServiceForm serviceForm = new ServiceForm();
        serviceForm.setServiceList(serviceRepository.findAll());
        serviceForm.setUnitList(unitRepository.findAll());
        model.addAttribute("serviceForm", serviceForm);
        model.addAttribute("units", unitRepository.findAll());
        return "admin_panel/system_settings/settings_services";
    }

    @PostMapping
    public String updateServices(@Valid @ModelAttribute ServiceForm serviceForm, BindingResult bindingResult,
                                 @RequestParam String[] new_service_names,
                                 @RequestParam String[] new_service_unit_names,
                                 @RequestParam(required = false) String[] new_service_show_in_meters,
                                 @RequestParam(required = false) String[] new_unit_names,
                                 RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) return "admin_panel/system_settings/settings_services";

        List<Service> serviceList = serviceForm.getServiceList();
        List<Unit> unitList = serviceForm.getUnitList().stream().filter((unit) -> unit.getId() != null).collect(Collectors.toList());

        serviceService.addNewUnits(unitList, new_unit_names);
        serviceService.addNewServices(serviceList, new_service_names, new_service_unit_names, new_service_show_in_meters);

        return "redirect:/admin/services";
    }

    @GetMapping("/delete/{id}")
    public String deleteService(@PathVariable long id, RedirectAttributes redirectAttributes) {
        try {
            serviceRepository.deleteById(id);
            return "redirect:/admin/services";
        } catch (Exception e) {
            e.printStackTrace();
            String fail_msg = "Нельзя удалить услугу \""+serviceService.getServiceNameById(id)+"\", она уже используется в расчетах!";
            redirectAttributes.addFlashAttribute("fail", fail_msg);
            return "redirect:/admin/services";
        }
    }

    @GetMapping("/delete-unit/{id}")
    public String deleteServiceUnit(@PathVariable long id, RedirectAttributes redirectAttributes) {
        try {
            unitRepository.deleteById(id);
            return "redirect:/admin/services";
        } catch (Exception e) {
            e.printStackTrace();
            String fail_msg = "Нельзя удалить единицу \""+serviceService.getUnitNameById(id)+"\", она уже используется в расчетах!";
            redirectAttributes.addFlashAttribute("fail", fail_msg);
            return "redirect:/admin/services";
        }
    }

    @GetMapping("/get-unit")
    public @ResponseBody Unit getUnitFromService(@RequestParam long id) {
        return serviceRepository.findById(id).orElseThrow().getUnit();
    }
}
