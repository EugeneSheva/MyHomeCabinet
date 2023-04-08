package com.example.myhome.home.controller;

import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.model.MeterData;
import com.example.myhome.home.model.MeterPaymentStatus;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.*;
import com.example.myhome.home.service.ApartmentService;
import com.example.myhome.home.service.BuildingService;
import com.example.myhome.home.service.MeterDataService;
import com.example.myhome.home.service.ServiceService;
import com.example.myhome.home.validator.MeterValidator;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/meters")
@Log
public class MeterController {

    @Autowired
    private MeterDataService meterDataService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private ApartmentService apartmentService;

    @Autowired private MeterValidator validator;

    //Получить все счетчики
    @GetMapping
    public String showMetersPage(Model model,
                                 @RequestParam(required = false) Long building,
                                 @RequestParam(required = false) String section,
                                 @RequestParam(required = false) Long apartment,
                                 @RequestParam(required = false) Long service) {

        List<Long> list = meterDataService.findMeterIds();
        List<MeterData> meterDataList;
//        List<MeterData> meterDataList = meterDataService.filter(meterDataService.findAllMetersById(list), building, section, apartment, service);

        if(building == null && section == null && apartment == null && service == null)
            meterDataList = meterDataService.findAllMetersById(meterDataService.findMeterIds());
        else meterDataList = meterDataService.findAllBySpecification(building, section, apartment, service);

        model.addAttribute("meter_data_rows", meterDataList);
        model.addAttribute("buildings", buildingService.findAllDTO());
        if(building != null) model.addAttribute("sections", buildingService.findById(building).getSections());
        model.addAttribute("services", serviceService.findAllServices());
        model.addAttribute("now", LocalDate.now());

        FilterForm form = new FilterForm();
        form.setBuilding(building);
        form.setService(service);
        form.setApartment(apartment);
        form.setSection(section);
        model.addAttribute("filter_form",form);
        return "admin_panel/meters/meters";
    }

    //Получить показания из какого-то одного счетчика
    @GetMapping("/data")
    public String showSingleMeterData(@RequestParam(required = false) Long flat_id,
                                      @RequestParam(required = false) Long service_id,
                                      Model model) {

        model.addAttribute("apart_number", apartmentService.findById(flat_id).getNumber());
        model.addAttribute("meter_data_rows", meterDataService.findSingleMeterData(flat_id, service_id));
        model.addAttribute("flat_id", flat_id);
        model.addAttribute("service_id", service_id);

        return "admin_panel/meters/meter_flat_data";
    }

    //Создать абсолютно новое показание
    @GetMapping("/create")
    public String showCreateMeterPage(Model model) {

        model.addAttribute("id",meterDataService.getMaxIdPlusOne());
        model.addAttribute("meterData", new MeterData());
        model.addAttribute("services", serviceService.findAllServices());
        model.addAttribute("buildings", buildingService.findAll());
        model.addAttribute("now", LocalDate.now());

        return "admin_panel/meters/meter_card";
    }

    //Создать новое показание для существующего счетчика
    @GetMapping("/create-add")
    public String showCreateAdditionalMeterPage(@RequestParam long flat_id, @RequestParam long service_id, Model model) {
        List<MeterData> meterDataList = meterDataService.findSingleMeterData(flat_id, service_id);
        MeterData meter = (meterDataList.isEmpty()) ? new MeterData() : meterDataList.get(meterDataList.size()-1);
        meter.setId(meter.getId()+1);
        model.addAttribute("id",meter.getId());
        model.addAttribute("meterData", meter);
        model.addAttribute("services", serviceService.findAllServices());
        model.addAttribute("buildings", buildingService.findAll());
        model.addAttribute("now", LocalDate.now());
        return "admin_panel/meters/meter_card";
    }

    @PostMapping("/create-add")
    public String alo(@Valid @ModelAttribute MeterData meterData) {
        MeterData savedMeter = meterDataService.saveMeterData(meterData);
        return "redirect:/admin/meters/data?flat_id="+savedMeter.getApartment().getId()+"&service_id="+savedMeter.getService().getId();
    }

    @PostMapping("/save-meter")
    public @ResponseBody String saveMeter(@RequestParam(required = false) Long initial_meter_id,    // <-- если хочешь обновить существующий
                                          @RequestParam String apartment_id,
                                          @RequestParam String readings,
                                          @RequestParam String stat,
                                          @RequestParam String service_id,
                                          @RequestParam String date) {

        MeterData savedMeter = meterDataService.saveMeterDataAJAX(initial_meter_id, apartment_id, readings, stat, service_id, date);
        return "SAVED METER";
    }


    @PostMapping("/create")
    public String createMeter(@ModelAttribute MeterData meterData, BindingResult bindingResult, Model model) {
        validator.validate(meterData, bindingResult);
        log.info(bindingResult.getAllErrors().toString());
        if(bindingResult.hasErrors()) {
            model.addAttribute("id",meterDataService.getMaxIdPlusOne());
            return "admin_panel/meters/meter_card";
        }
        MeterData savedMeter = meterDataService.saveMeterData(meterData);
        return "redirect:/admin/meters/data?flat_id="+savedMeter.getApartment().getId()+"&service_id="+savedMeter.getService().getId();
    }

    //Создать новое показание для заданной квартиры/счетчика
    @GetMapping("/create-additional")
    public String showCreateAdditionalMeterPage(@RequestParam long flat_id, Model model) {

        model.addAttribute("id", meterDataService.getMaxIdPlusOne());
        model.addAttribute("meterData", new MeterData());
        model.addAttribute("apartment", apartmentService.findById(flat_id));
        model.addAttribute("services", serviceService.findAllServices());
        model.addAttribute("buildings", buildingService.findAll());
        model.addAttribute("now", LocalDate.now());

        return "admin_panel/meters/meter_card";
    }

    @GetMapping("/update/{id}")
    public String showUpdateMeterPage(@PathVariable long id, Model model) {

        model.addAttribute("meterData", meterDataService.findMeterData(id));
        model.addAttribute("services", serviceService.findAllServices());
        model.addAttribute("buildings", buildingService.findAll());
        model.addAttribute("now", LocalDate.now());

        return "admin_panel/meters/meter_card";
    }

    @PostMapping("/update/{id}")
    public String updateMeter(@PathVariable long id, @ModelAttribute MeterData meterData, BindingResult bindingResult, Model model) {
        validator.validate(meterData, bindingResult);
        log.info(bindingResult.getAllErrors().toString());
        if(bindingResult.hasErrors()) {
            model.addAttribute("id",meterDataService.getMaxIdPlusOne());
            return "admin_panel/meters/meter_card";
        }
        MeterData savedMeter = meterDataService.saveMeterData(meterData);
        return "redirect:/admin/meters/data?flat_id="+savedMeter.getApartment().getId()+"&service_id="+savedMeter.getService().getId();
    }

    @GetMapping("/delete/{id}")
    public String deleteMeter(@PathVariable long id,
                              @RequestHeader(value = HttpHeaders.REFERER, required = false) final String referrer) {
        meterDataService.deleteMeter(id);
        return "redirect:" + referrer;
    }

    @GetMapping("/info/{id}")
    public String showInfo(@PathVariable long id, Model model) {
        model.addAttribute("meter", meterDataService.findMeterData(id));
        return "admin_panel/meters/meter_profile";
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("id",meterDataService.getMaxIdPlusOne());
        model.addAttribute("services", serviceService.findAllServices());
        model.addAttribute("buildings", buildingService.findAll());
        model.addAttribute("now", LocalDate.now());
    }


}
