package com.example.myhome.home.controller.admin_panel;

import com.example.myhome.home.model.Service;
import com.example.myhome.home.model.Tariff;
import com.example.myhome.home.repository.ServiceRepository;
import com.example.myhome.home.repository.TariffRepository;
import com.example.myhome.home.repository.UnitRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Controller
@RequestMapping("/admin/tariffs")
@Log
public class TariffController {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private TariffRepository tariffRepository;

    @GetMapping
    public String showTariffsPage(Model model) {
        model.addAttribute("tariffs", tariffRepository.findAll());
        return "settings_tariffs";
    }

    @GetMapping("/{id}")
    public String showTariffInfoPage(@PathVariable long id, Model model) {
        model.addAttribute("tariff", tariffRepository.findById(id).orElseThrow());
        return "tariff_profile";
    }

    @GetMapping("/create")
    public String showCreateTariffCard(Model model){
        model.addAttribute("tariff", new Tariff());
        model.addAttribute("services", serviceRepository.findAll());
        model.addAttribute("units", unitRepository.findAll());
        return "tariff_card";
    }

    @PostMapping("/create")
    public String createTariff(@RequestParam String name,
                               @RequestParam String description,
                               @RequestParam String[] service_names,
                               @RequestParam String[] prices) {
        Tariff tariff = new Tariff();
        tariff.setName(name);
        tariff.setDescription(description);
        tariff.setDate(LocalDateTime.now());
        tariff.setTariffComponents(new HashMap<>());

        for (int i = 0; i < service_names.length; i++) {
            log.info("cycle");
            if(service_names[i].isEmpty() || prices[i].isEmpty()) continue;
            log.info(service_names[i]);
            log.info(prices[i]);
            tariff.getTariffComponents().put(serviceRepository.findByName(service_names[i]).orElseThrow(),
                    Double.parseDouble(prices[i]));
            log.info(tariff.getTariffComponents().toString());
        }

        log.info(tariff.toString());

        tariffRepository.save(tariff);

        return "redirect:/admin/tariffs";
    }

    @GetMapping("/update/{id}")
    public String showUpdateTariffPage(@PathVariable long id, Model model) {
        Tariff tariff = tariffRepository.findById(id).orElseGet(Tariff::new);
        model.addAttribute("tariff", tariff);
        model.addAttribute("components", tariff.getTariffComponents().entrySet());
        model.addAttribute("services", serviceRepository.findAll());
        model.addAttribute("units", unitRepository.findAll());
        return "tariff_card";
    }

    @PostMapping("/update/{id}")
    public String updateTariff(@PathVariable long id,
                               @RequestParam String name,
                               @RequestParam String description,
                               @RequestParam(defaultValue = "0", required = false) String[] service_names,
                               @RequestParam(defaultValue = "0", required = false) String[] prices) {
        Tariff tariff = new Tariff();
        tariff.setId(id);
        tariff.setName(name);
        tariff.setDescription(description);
        tariff.setDate(LocalDateTime.now());
        tariff.setTariffComponents(new HashMap<>());

        log.info(Arrays.toString(service_names));
        log.info(Arrays.toString(prices));

        for (int i = 0; i < service_names.length; i++) {
            log.info("cycle");
            if(service_names[i].isEmpty() || prices[i].isEmpty()) continue;
            log.info(service_names[i]);
            log.info(prices[i]);
            tariff.getTariffComponents().put(serviceRepository.findByName(service_names[i]).orElseThrow(),
                    Double.parseDouble(prices[i]));
            log.info(tariff.getTariffComponents().toString());
        }

        log.info(tariff.toString());

        tariffRepository.save(tariff);

        return "redirect:/admin/tariffs";
    }

    @GetMapping("/delete/{id}")
    public String deleteTariff(@PathVariable long id) {
        tariffRepository.deleteById(id);
        return "redirect:/admin/tariffs";
    }

    // ===========

    @GetMapping("/get-components")
    public @ResponseBody Map<String, Double> getTariffComponents(@RequestParam long tariff_id) throws JsonProcessingException {
        Map<Service, Double> components = tariffRepository.findById(tariff_id).orElseThrow().getTariffComponents();
        Map<String, Double> comp_2 = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        for(Map.Entry<Service, Double> entry : components.entrySet()) {
            comp_2.put(mapper.writeValueAsString(entry.getKey()), entry.getValue());
        }
        return comp_2;
    }

}
