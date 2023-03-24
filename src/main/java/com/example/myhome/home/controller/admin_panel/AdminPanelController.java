package com.example.myhome.home.controller.admin_panel;

import com.example.myhome.home.model.Message;
import com.example.myhome.home.model.MeterData;
import com.example.myhome.home.model.RepairRequest;
import com.example.myhome.home.repos.MeterDataRepository;
import com.example.myhome.home.repos.RepairRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminPanelController {

    @Autowired
    private MeterDataRepository meterDataRepository;

    @Autowired
    private RepairRequestRepository repairRequestRepository;

    @GetMapping("/messages")
    public String showMessagesPage(Model model) {return "messages";}

    @GetMapping("/messages/create")
    public String showCreateMessagePage(Model model) {
        model.addAttribute("message", new Message());
        return "message_card";
    }

    @GetMapping("/requests")
    public String showRequestsPage(Model model) {return "requests";}

    @GetMapping("/requests/create")
    public String showCreateRequestPage(Model model) {
        model.addAttribute("request", new RepairRequest());
        return "request_card";
    }

    @GetMapping("/requests/update/{id}")
    public String showUpdateRequestPage(@PathVariable long id, Model model) {
        model.addAttribute("request", repairRequestRepository.findById(id).orElseGet(RepairRequest::new));
        return "request_card";
    }

    @GetMapping("/meters")
    public String showMetersPage(Model model) {return "meters";}

    @GetMapping("/meters/create")
    public String showCreateMeterPage(Model model) {
        //Добавить дома, потом их секции, потом квартиры
        //model.addAttribute("buildings", );
        model.addAttribute("meter", new MeterData());
        return "meter_card";
    }

    @GetMapping("/meters/update/{id}")
    public String showUpdateMeterPage(@PathVariable long id, Model model) {
        model.addAttribute("meter", meterDataRepository.findById(id).orElseGet(MeterData::new));
        return "meter_card";
    }

    @GetMapping("/meters/data")
    public String showSingleMeterDataPage(@RequestParam long id, Model model) {
        model.addAttribute("meter", meterDataRepository.findById(id).orElseGet(MeterData::new));
        return "meter_card";
    }
}
