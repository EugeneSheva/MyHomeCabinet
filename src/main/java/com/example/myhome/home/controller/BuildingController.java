package com.example.myhome.home.controller;

import com.example.myhome.home.model.Building;
import com.example.myhome.home.service.BuildingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/buildings")
public class BuildingController {

    private final BuildingService buildingService;
    @GetMapping("/")
    public String getBuildigs(Model model) {
        List<Building> buildingList = buildingService.findAll();
        model.addAttribute("buildings", buildingList);
        return "admin_panel/buildings";
    }

    @GetMapping("/{id}")
    public String getBuildig(@PathVariable("id") Long id, Model model) {
        Building building = buildingService.findById(id);
        model.addAttribute("building", building);
        return "admin_panel/building";
    }

}
