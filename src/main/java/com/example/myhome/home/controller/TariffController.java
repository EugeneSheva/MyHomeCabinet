package com.example.myhome.home.controller;

import com.example.myhome.home.model.Service;
import com.example.myhome.home.model.Tariff;
import com.example.myhome.home.service.impl.ServiceServiceImpl;
import com.example.myhome.home.service.TariffService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/admin/tariffs")
@Log
public class TariffController {

    @Autowired private ServiceServiceImpl serviceService;
    @Autowired private TariffService tariffService;

    @GetMapping
    public String showTariffsPage(@RequestParam(required = false) String sort, Model model) {
        List<Tariff> tariffs = new ArrayList<>();
        if(sort != null) {
            if(sort.equalsIgnoreCase("asc")) {
                tariffs = tariffService.findAllTariffsSorted(true);
                model.addAttribute("sort", "desc");
            }
            else if(sort.equalsIgnoreCase("desc")) {
                tariffs = tariffService.findAllTariffsSorted(false);
                model.addAttribute("sort", "asc");
            }
        } else {
            tariffs = tariffService.findAllTariffs();
            model.addAttribute("sort", "asc");
        }
        model.addAttribute("tariffs", tariffs);
        return "admin_panel/system_settings/settings_tariffs";
    }

    @GetMapping("/{id}")
    public String showTariffInfoPage(@PathVariable long id, Model model) {
        model.addAttribute("tariff", tariffService.findTariffById(id));
        return "admin_panel/system_settings/tariff_profile";
    }

    @GetMapping("/create")
    public String showCreateTariffCard(Model model){
        model.addAttribute("tariff", new Tariff());
        model.addAttribute("services", serviceService.findAllServices());
        model.addAttribute("units", serviceService.findAllUnits());
        return "admin_panel/system_settings/tariff_card";
    }

    @PostMapping("/create")
    public String createTariff(@Valid @ModelAttribute Tariff tariff,
                               BindingResult bindingResult,
                               @RequestParam(required = false) String[] service_names,
                               @RequestParam(required = false) String[] prices) {

        if(bindingResult.hasErrors()) {
            log.info("errors found");
            log.info(bindingResult.getObjectName());
            log.info(bindingResult.getAllErrors().toString());
            return "admin_panel/system_settings/tariff_card";
        }

        tariff.setDate(LocalDateTime.now());
        tariff.setTariffComponents(tariffService.buildComponentsMap(service_names, prices));

        log.info(tariff.toString());

        tariffService.saveTariff(tariff);

        return "redirect:/admin/tariffs";
    }

    @GetMapping("/update/{id}")
    public String showUpdateTariffPage(@PathVariable long id, Model model) {
        Tariff tariff = tariffService.findTariffById(id);
        model.addAttribute("tariff", tariff);
        model.addAttribute("components", tariff.getTariffComponents().entrySet());
        model.addAttribute("services", serviceService.findAllServices());
        model.addAttribute("units", serviceService.findAllUnits());
        return "admin_panel/system_settings/tariff_card";
    }

    @PostMapping("/update/{id}")
    public String updateTariff(@PathVariable long id,
                               @Valid @ModelAttribute Tariff tariff,
                               BindingResult bindingResult,
                               @RequestParam(defaultValue = "0", required = false) String[] service_names,
                               @RequestParam(defaultValue = "0", required = false) String[] prices) {

        if(bindingResult.hasErrors()) {
            log.info("errors found");
            log.info(bindingResult.getObjectName());
            log.info(bindingResult.getAllErrors().toString());
            return "admin_panel/system_settings/tariff_card";
        }

        tariff.setId(id);
        tariff.setDate(LocalDateTime.now());
        tariff.setTariffComponents(tariffService.buildComponentsMap(service_names, prices));

        log.info(tariff.toString());

        tariffService.saveTariff(tariff);

        return "redirect:/admin/tariffs";
    }

    @GetMapping("/delete/{id}")
    public String deleteTariff(@PathVariable long id) {
        tariffService.deleteTariffById(id);
        return "redirect:/admin/tariffs";
    }

    // ===========

    @GetMapping("/get-components")
    public @ResponseBody Map<String, Double> getTariffComponents(@RequestParam long tariff_id) throws JsonProcessingException {
        Map<Service, Double> components = tariffService.findTariffById(tariff_id).getTariffComponents();
        Map<String, Double> comp_2 = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        for(Map.Entry<Service, Double> entry : components.entrySet()) {
            comp_2.put(mapper.writeValueAsString(entry.getKey()), entry.getValue());
        }
        return comp_2;
    }

}
