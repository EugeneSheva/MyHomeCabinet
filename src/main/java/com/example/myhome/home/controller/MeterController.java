package com.example.myhome.home.controller;

import com.example.myhome.home.model.MeterData;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.service.ApartmentService;
import com.example.myhome.home.service.BuildingService;
import com.example.myhome.home.service.MeterDataService;
import com.example.myhome.home.service.ServiceService;
import com.example.myhome.home.validator.MeterValidator;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
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
                                 @RequestParam(required = false) Long service,
                                 @RequestParam(required = false) Integer page) {

        if(page == null) return "redirect:/admin/meters?page=1";

        Page<MeterData> meterDataList;

        meterDataList = meterDataService.findAllBySpecificationAndPage(building, section, apartment, service, page);

        log.info(meterDataList.toString());

        model.addAttribute("meter_data_rows", meterDataList.getContent());
        model.addAttribute("pages_count", meterDataList.getTotalPages());

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
                                      Model model,
                                      FilterForm form) {

        form.setApartment(flat_id);
        form.setService(service_id);

        log.info(form.toString());

        List<MeterData> meterDataPage = meterDataService.findSingleMeterData(form);

        log.info(meterDataPage.toString());

        model.addAttribute("meter_data_rows", meterDataPage);
        model.addAttribute("filter_form", form);
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
        meter.setId(meterDataService.getMaxIdPlusOne());
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

//    @PostMapping("/save-meter")
//    public @ResponseBody List<String> saveMeter(@RequestParam(required = false) Long id,    // <-- если хочешь обновить существующий
//                                          @RequestParam String building,
//                                          @RequestParam String section,
//                                          @RequestParam String apartment,
//                                          @RequestParam String currentReadings,
//                                          @RequestParam String status,
//                                          @RequestParam String service,
//                                          @RequestParam String date) {
//
//        MeterData meterToSave = meterDataService.saveMeterDataAJAX(id, building, section,
//                apartment, currentReadings, status, service, date);
//        log.info(meterToSave.toString());
//        DataBinder binder = new DataBinder(meterToSave);
//        binder.setValidator(validator);
//        binder.validate();
//        if(binder.getBindingResult().hasErrors()) {
//            List<ObjectError> messages = binder.getBindingResult().getAllErrors();
//            List<String> msgs =
//                    messages.stream()
//                            .map(ObjectError::getDefaultMessage)
//                            .filter(Objects::nonNull)
//                            .toList();
//            log.info(msgs.toString());
//            log.info(String.valueOf(msgs.size()));
//            return msgs;
//        }
//        meterDataService.saveMeterData(meterToSave);
//        return null;
//    }

//    @PostMapping("/save-meter")
//    public String saveMeter(@RequestBody MeterData meterData,
//                          BindingResult bindingResult,
//                          Model model) {
//        log.info("Пришедший счетчик: ");
//        log.info(meterData.toString());
//        log.info("Валидируем");
//        validator.validate(meterData, bindingResult);
//        log.info("Валидация окончена");
//        if(bindingResult.hasErrors()){
//            log.info("Errors found");
//            log.info(bindingResult.getAllErrors().toString());
//            return "admin_panel/meters/meter_card";
//        }
//        log.info("Ошибок нет");
//        MeterData savedMeter = meterDataService.saveMeterData(meterData);
//        log.info("Сохранили");
//
//        return "redirect:/admin/meters/create-add?flat_id="+savedMeter.getApartment().getId()+"&service_id="+savedMeter.getService().getId();
//    }

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
        MeterData meter = meterDataService.findMeterData(id);
        model.addAttribute("meterData", meter);
        model.addAttribute("id",meter.getId());
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
    }


}
