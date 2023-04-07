package com.example.myhome.home.controller;

import com.example.myhome.home.model.*;
import com.example.myhome.home.repository.*;
import com.example.myhome.home.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
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
    private final CashBoxService cashBoxService;
    private final AccountRepository accountRepository;
    private final ApartmentRepository apartmentRepository;
    private final InvoiceService invoiceService;

    @GetMapping("/")
    public String getOwners(Model model) {
        model.addAttribute("newRepairRequestQuant", repairRequestRepository.countRepairRequestsByStatus(RepairStatus.ACCEPTED));
        model.addAttribute("inworkRepairRequestQuant", repairRequestRepository.countRepairRequestsByStatus(RepairStatus.IN_WORK));
        model.addAttribute("ownersQuant", ownerService.getQuantity());
        model.addAttribute("buidingsQuant", buildingService.getQuantity());
        model.addAttribute("apartmentsQuant", apartmentService.getQuantity());
        model.addAttribute("accountsQuant", accountService.getQuantity());

        model.addAttribute("cashBoxSum", cashBoxRepository.sumAmount());
        model.addAttribute("accountBalance", accountRepository.getSumOfAccountBalances());
        model.addAttribute("sumDebt", accountRepository.getSumOfAccountDebts());

        model.addAttribute("diagramIncomeCashBox", cashBoxService.getListSumIncomeByMonth());
        model.addAttribute("diagramExpenceCashBox", cashBoxService.getListSumExpenceByMonth());
        model.addAttribute("diagramMonths", cashBoxService.getListOfMonthName());

        model.addAttribute("diagramAllInvoices", invoiceService.getListSumInvoicesByMonth());
        model.addAttribute("diagramPaidInvoices", invoiceService.getListSumPaidInvoicesByMonth());

//        List<Apartment>apartmentList = apartmentRepository.findByFilters(7L, null, null, null, null);
//        System.out.println("no 7 "+ apartmentList);
//
//        List<Apartment>apartmentList2 = apartmentRepository.findByFilters(null, null, "2",null,null);
//        System.out.println("секц 2 "+ apartmentList2);
//
//        List<Apartment>apartmentList3 = apartmentRepository.findByFilters(null, "ЖК Мир", null, null, null);
//        System.out.println("Дом Мир "+ apartmentList3);
//
//        List<Apartment>apartmentList4 = apartmentRepository.findByFilters(null, "ЖК Престиж", null, null, null);
//        System.out.println("Дом Прест "+ apartmentList4);

//        List<Apartment>apartmentList5 = apartmentRepository.findByFilters(null, null, null, null, 1L,null);
//        System.out.println("Владелец1  "+ apartmentList5);
//
//        List<Apartment>apartmentList6 = apartmentRepository.findByFilters(null, null, null, null, 2L,null);
//        System.out.println("Владелец2  "+ apartmentList6);

//        List<Apartment>apartmentList7 = apartmentRepository.findByFilters(null, null, null, null, null,"debt");
//        System.out.println("Есть долг  "+ apartmentList7);
//
//        List<Apartment>apartmentList8 = apartmentRepository.findByFilters(null, null, null, null, null,"nodebt");
//        System.out.println("Нет долга  "+ apartmentList8);

;

        return "admin_panel/statistics/statistics";
    }
}
