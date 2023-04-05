package com.example.myhome.home.controller;

import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.model.Building;
import com.example.myhome.home.model.BuildingDTO;
import com.example.myhome.home.service.BuildingService;
import com.example.myhome.home.validator.BuildingValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/buildings")
public class BuildingController {


    @Value("${upload.path}")
    private String uploadPath;
    private final BuildingService buildingService;

    private BuildingValidator buildingValidator;
    @GetMapping("/")
    public String getBuildigs(Model model) {
        List<Building> buildingList = buildingService.findAll();
        model.addAttribute("buildings", buildingList);
        return "admin_panel/buildings/buildings";
    }

    @GetMapping("/{id}")
    public String getBuildig(@PathVariable("id") Long id, Model model) {
        Building building = buildingService.findById(id);
        model.addAttribute("building", building);
        return "admin_panel/buildings/building";
    }

    @GetMapping("/new")
    public String createBuildig(Model model) {
        Building building = new Building();
        model.addAttribute("building", building);
        return "admin_panel/buildings/building_edit";
    }
    @GetMapping("edit/{id}")
    public String editBuildig(@PathVariable("id") Long id, Model model) {
        Building building = new Building();
        model.addAttribute("building", building);
        return "admin_panel/buildings/building_edit";
    }

    @PostMapping("/save")
    public String saveBuildig(@ModelAttribute("building") Building build, BindingResult bindingResult, @RequestParam("name") String name, @RequestParam("address") String address,
                              @RequestParam("sections") List<String> sections, @RequestParam(name = "id", defaultValue = "0") Long id, @RequestParam("floors") List<String> floors, @RequestParam("img1") MultipartFile file1,
                              @RequestParam("img2") MultipartFile file2, @RequestParam("img3") MultipartFile file3, @RequestParam("img4") MultipartFile file4,
                              @RequestParam("img5") MultipartFile file5) throws IOException {
            if (bindingResult.hasErrors()) {
                System.out.println(bindingResult.toString());;
                return "admin_panel/buildings/building_edit";
            } else {
                Building building = buildingService.saveBuildindImages(id, file1, file2, file3, file4, file5);
                building.setName(name);
                building.setAddress(address);
                building.setFloors(floors);
                building.setSections(sections);

                buildingService.save(building);
                return "redirect:/admin/buildings/";
            }
    }

    @GetMapping("/delete/{id}")
    public String dellete(@PathVariable("id") Long id) {
        Building building = buildingService.findById(id);
        try {
            Files.deleteIfExists(Path.of(uploadPath + building.getImg1()));
            Files.deleteIfExists(Path.of(uploadPath + building.getImg2()));
            Files.deleteIfExists(Path.of(uploadPath + building.getImg3()));
            Files.deleteIfExists(Path.of(uploadPath + building.getImg4()));
            Files.deleteIfExists(Path.of(uploadPath + building.getImg5()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        buildingService.deleteById(id);
        return "redirect:/admin/buildings/";
    }

    @GetMapping("/get-sections/{id}")
    public @ResponseBody List<String> getBuildingSections(@PathVariable long id) {
        return buildingService.findById(id).getSections();
    }

    @GetMapping("/get-section-apartments")
    public @ResponseBody List<Apartment> getBuildingSectionApartments(@RequestParam long id, @RequestParam String section_name) {

        List<Apartment> apartments = buildingService.findById(id).getApartments();
        return apartments.stream().filter((apartment) -> apartment.getSection().equals(section_name)).collect(Collectors.toList());

    }
    @GetMapping("/get-section-apartments/{id}")
    public @ResponseBody List<Apartment> getBuildingSectionApartmentsFromQuery(@PathVariable long id, @RequestParam String section_name) {
        return buildingService.getSectionApartments(id, section_name);
    }
}
