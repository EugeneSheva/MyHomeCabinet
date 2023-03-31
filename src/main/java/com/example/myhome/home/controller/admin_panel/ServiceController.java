package com.example.myhome.home.controller.admin_panel;

import com.example.myhome.home.model.Service;
import com.example.myhome.home.model.ServiceForm;
import com.example.myhome.home.model.Unit;
import com.example.myhome.home.repository.ServiceRepository;
import com.example.myhome.home.repository.UnitRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping
    public String showServicesPage(Model model){
        ServiceForm serviceForm = new ServiceForm();
        serviceForm.setServiceList(serviceRepository.findAll());
        serviceForm.setUnitList(unitRepository.findAll());
        model.addAttribute("serviceForm", serviceForm);
        model.addAttribute("units", unitRepository.findAll());
        return "settings_services2";
    }

    @PostMapping
    public String updateServices(@ModelAttribute ServiceForm serviceForm,
                                 @RequestParam String[] new_service_names,
                                 @RequestParam String[] new_service_unit_names,
                                 @RequestParam(required = false) boolean[] new_service_show_in_meters,
                                 @RequestParam(required = false) String[] new_unit_names,
                                 RedirectAttributes redirectAttributes) {
        List<Service> serviceList = serviceForm.getServiceList();
        List<Unit> unitList = serviceForm.getUnitList().stream().filter((unit) -> unit.getId() != null).collect(Collectors.toList());

        log.info(unitList.toString());

        if(new_unit_names != null) {

            log.info(Arrays.toString(new_unit_names));

            for (int i = 0; i < new_unit_names.length-1; i++) {
                log.info("creating new unit");
                Unit unit = new Unit();
                unit.setName(new_unit_names[i]);
                unitList.add(unit);
            }
        }

        log.info(unitList.toString());

        unitRepository.saveAll(unitList);
        unitList.forEach(System.out::println);

        for (int i = 0; i < new_service_names.length-1; i++) {
            Service service = new Service();
            service.setName(new_service_names[i]);
            service.setShow_in_meters(new_service_show_in_meters[i]);
            service.setUnit(unitRepository.findByName(new_service_unit_names[i]).orElseGet(Unit::new));
            serviceList.add(service);
        }

        serviceRepository.saveAll(serviceList);
        serviceList.forEach(System.out::println);

        return "redirect:/admin/services";
    }

    @GetMapping("/delete/{id}")
    public String deleteService(@PathVariable long id, RedirectAttributes redirectAttributes) {
        try {
            serviceRepository.deleteById(id);
            return "redirect:/admin/services";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("fail", "Нельзя удалить услугу, она уже где-то используется");
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
            redirectAttributes.addFlashAttribute("fail", "Нельзя удалить единицу");
            return "redirect:/admin/services";
        }
    }

    @GetMapping("/get-unit")
    public @ResponseBody Unit getUnitFromService(@RequestParam long id) {
        return serviceRepository.findById(id).orElseThrow().getUnit();
    }
}
