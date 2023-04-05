package com.example.myhome.home.controller;

import com.example.myhome.home.model.ApartmentAccount;
import com.example.myhome.home.model.Owner;
import com.example.myhome.home.model.RepairStatus;
import com.example.myhome.home.repository.*;
import com.example.myhome.home.service.AccountService;
import com.example.myhome.home.service.ApartmentService;
import com.example.myhome.home.service.BuildingService;
import com.example.myhome.home.service.OwnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/statistics")
@Log
public class StatisticsController {

    private final RepairRequestRepository repairRequestRepository;
    private final OwnerService ownerService;
    private final BuildingService buildingService;
    private final ApartmentService apartmentService;
    private final AccountService accountService;
    private final CashBoxRepository cashBoxRepository;
    private final AccountRepository accountRepository;

    @GetMapping("/")
    public String getOwners(Model model) {
        model.addAttribute("newRepairRequestQuant", repairRequestRepository.countRepairRequestsByStatus(RepairStatus.ACCEPTED));
        model.addAttribute("inworkRepairRequestQuant", repairRequestRepository.countRepairRequestsByStatus(RepairStatus.IN_WORK));
        model.addAttribute("ownersQuant", ownerService.getQuantity());
        model.addAttribute("buidingsQuant", buildingService.getQuantity());
        model.addAttribute("apartmentsQuant", apartmentService.getQuantity());
        model.addAttribute("accountsQuant", accountService.getQuantity());
        model.addAttribute("cashBoxSum", cashBoxRepository.sumAmount());

        model.addAttribute("accountBalance", accountRepository.getBalance());
        model.addAttribute("sumDebt", accountRepository.getDebt());

        return "admin_panel/statistics/statistics";
    }
}
