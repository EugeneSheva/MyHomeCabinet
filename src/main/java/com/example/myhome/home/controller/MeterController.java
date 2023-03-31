package com.example.myhome.home.controller;

import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.model.MeterData;
import com.example.myhome.home.model.MeterPaymentStatus;
import com.example.myhome.home.repository.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/meters")
@Log
public class MeterController {

    @Autowired
    private MeterDataRepository meterDataRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private ApartmentRepository apartmentRepository;

    //Получить все счетчики
    @GetMapping
    public String showMetersPage(Model model) {
        List<Long> list = meterDataRepository.findDistinctGroupByApartmentAndService();
        List<MeterData> meterDataList = meterDataRepository.findAllById(list);
        log.info(meterDataList.toString());
        model.addAttribute("meter_data_rows", meterDataList);
        model.addAttribute("now", LocalDate.now());
        return "meters";
    }

    //Получить показания из какого-то одного счетчика
    @GetMapping("/data")
    public String showSingleMeterData(@RequestParam long flat_id, @RequestParam long service_id, Model model) {
        List<MeterData> meterDataList = meterDataRepository.findSingleMeterData(flat_id, service_id);
        log.info(meterDataList.toString());
        long apartment_number = apartmentRepository.findById(flat_id).orElseThrow().getNumber();
        model.addAttribute("apart_number", apartment_number);
        model.addAttribute("meter_data_rows", meterDataList);
        model.addAttribute("flat_id", flat_id);
        model.addAttribute("service_id", service_id);
        return "meter_flat_data";
    }

    //Создать абсолютно новое показание
    @GetMapping("/create")
    public String showCreateMeterPage(Model model) {
        log.info("SDLKFJLDSKJDSKLF");
        long newId = 0L;
        Optional<MeterData> meterWithBiggestId = meterDataRepository.findFirstByOrderByIdDesc();
        if(meterWithBiggestId.isEmpty()) newId = 1L;
        else newId = meterWithBiggestId.get().getId()+1;
        log.info("new id for meter data: " + newId);
        model.addAttribute("id",newId);
        model.addAttribute("meter", new MeterData());
        model.addAttribute("services", serviceRepository.findAll());
        model.addAttribute("buildings", buildingRepository.findAll());
        model.addAttribute("now", LocalDate.now());
        return "meter_card";
    }

    //Создать новое показание для существующего счетчика
    @GetMapping("/create-add")
    public String showCreateAdditionalMeterPage(@RequestParam long flat_id, @RequestParam long service_id, Model model) {
        List<MeterData> meterDataList = meterDataRepository.findSingleMeterData(flat_id, service_id);
        MeterData meter = (meterDataList.isEmpty()) ? new MeterData() : meterDataList.get(meterDataList.size()-1);
        log.info(meter.toString());
        long newId;
        Optional<MeterData> meterWithBiggestId = meterDataRepository.findFirstByOrderByIdDesc();
        if(meterWithBiggestId.isEmpty()) newId = 1L;
        else newId = meterWithBiggestId.get().getId()+1;
        log.info("new id for meter data: " + newId);
        model.addAttribute("id",newId);
        model.addAttribute("meter", meter);
        model.addAttribute("services", serviceRepository.findAll());
        model.addAttribute("buildings", buildingRepository.findAll());
        model.addAttribute("now", LocalDate.now());
        return "meter_card";
    }

    @PostMapping("/create-add")
    public String alo(@ModelAttribute MeterData meter) {
        log.info("ALO");
        log.info(meter.toString());
        MeterData savedMeter = meterDataRepository.save(meter);
        log.info("Saved meter: " + savedMeter);
        return "redirect:/admin/meters/data?flat_id="+savedMeter.getApartment().getId()+"&service_id="+savedMeter.getService().getId();
    }

    @PostMapping("/save-meter")
    public @ResponseBody String saveMeter(@RequestParam String apartment_id,
                                          @RequestParam String readings,
                                          @RequestParam String stat,
                                          @RequestParam String service_id,
                                          @RequestParam String date) {
        log.info("ALO");

        MeterData newMeter = new MeterData();
        newMeter.setApartment(apartmentRepository.findById(Long.parseLong(apartment_id)).orElseThrow());
        newMeter.setCurrentReadings(Double.parseDouble(readings));
        newMeter.setStatus(MeterPaymentStatus.valueOf(stat));
        newMeter.setService(serviceRepository.findById(Long.parseLong(service_id)).orElseThrow());
        newMeter.setDate(LocalDate.parse(date));

        meterDataRepository.save(newMeter);

        return "SAVED METER";
    }


    @PostMapping("/create")
    public String createMeter(@ModelAttribute MeterData meter) {
        MeterData savedMeter = meterDataRepository.save(meter);
        log.info("Saved meter data");
        System.out.println(savedMeter);
        System.out.println(savedMeter.getApartment().toString());
        return "redirect:/admin/meters/data?flat_id="+savedMeter.getApartment().getId()+"&service_id="+savedMeter.getService().getId();

    }

    //Создать новое показание для заданной квартиры/счетчика
    @GetMapping("/create-additional")
    public String showCreateAdditionalMeterPage(@RequestParam long flat_id, Model model) {
        long newId = 0L;
        Apartment apartment = apartmentRepository.findById(flat_id).orElseThrow();
        Optional<MeterData> meterWithBiggestId = meterDataRepository.findFirstByOrderByIdDesc();
        if(meterWithBiggestId.isEmpty()) newId = 1L;
        else newId = meterWithBiggestId.get().getId()+1;
        model.addAttribute("id",newId);
        model.addAttribute("meter", new MeterData());
        model.addAttribute("apartment", apartment);
        model.addAttribute("services", serviceRepository.findAll());
        model.addAttribute("buildings", buildingRepository.findAll());
        model.addAttribute("now", LocalDate.now());
        return "meter_card";
    }

    @GetMapping("/update/{id}")
    public String showUpdateMeterPage(@PathVariable long id, Model model) {
        model.addAttribute("meter", meterDataRepository.findById(id).orElseGet(MeterData::new));
        model.addAttribute("services", serviceRepository.findAll());
        model.addAttribute("buildings", buildingRepository.findAll());
        model.addAttribute("now", LocalDate.now());
        return "meter_card";
    }

    @PostMapping("/update/{id}")
    public String updateMeter(@PathVariable long id, @ModelAttribute MeterData meter) {
        MeterData savedMeter = meterDataRepository.save(meter);
        log.info("Saved meter data");
        System.out.println(savedMeter);
        return "redirect:/admin/meters/data?flat_id="+savedMeter.getApartment().getId()+"&service_id="+savedMeter.getService().getId();
    }

    @GetMapping("/delete/{id}")
    public String deleteMeter(@PathVariable long id,
                            @RequestHeader(value = HttpHeaders.REFERER, required = false) final String referrer) {
        meterDataRepository.deleteById(id);
        log.info("Deleted meter");
        return "redirect:" + referrer;
    }

    @GetMapping("/info/{id}")
    public String showInfo(@PathVariable long id, Model model) {
        MeterData meter = meterDataRepository.findById(id).orElseThrow();
        log.info(meter.toString());
        model.addAttribute("meter", meter);
        return "meter_profile";
    }


}
