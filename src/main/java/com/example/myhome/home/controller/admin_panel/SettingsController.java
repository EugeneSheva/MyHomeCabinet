package com.example.myhome.home.controller.admin_panel;

import com.example.myhome.home.model.*;
import com.example.myhome.home.repository.*;
import com.example.myhome.util.UserRole;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Log
public class SettingsController {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private IncomeExpenseRepository incomeExpenseRepository;

    @Autowired
    private TariffRepository tariffRepository;

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/admin/payment-details")
    public String showPaymentDetailsPage(Model model) {
        model.addAttribute("details", paymentDetailsRepository.findById(1L).orElseGet(PaymentDetails::new));
        return "admin_panel/system_settings/settings_payment";
    }

    @GetMapping("/admin/income-expense")
    public String showTransactionsPage(Model model) {
        model.addAttribute("transactions", incomeExpenseRepository.findAll());
        return "admin_panel/system_settings/settings_inc_exp";
    }

    @GetMapping("/admin/income-expense/create")
    public String showCreateTransactionPage(Model model) {
        model.addAttribute("transaction", new IncomeExpenseItems());
        return "admin_panel/system_settings/transaction_card";
    }

    @PostMapping("/admin/income-expense/create")
    public String createTransaction(@ModelAttribute IncomeExpenseItems item) {
        if(!incomeExpenseRepository.existsByName(item.getName()))
            incomeExpenseRepository.save(new IncomeExpenseItems(item.getName(), item.getIncomeExpenseType()));
        return "redirect:/admin/income-expense";
    }

    @GetMapping("/admin/income-expense/update/{id}")
    public String showUpdateTransactionPage(@PathVariable long id, Model model) {
        model.addAttribute("transaction", incomeExpenseRepository.findById(id).orElseThrow());
        return "admin_panel/system_settings/transaction_card";
    }

    @PostMapping("/admin/income-expense/update/{id}")
    public String updateTransaction(@ModelAttribute IncomeExpenseItems item) {
        incomeExpenseRepository.save(item);
        return "redirect:/admin/income-expense";
    }

    @GetMapping("/admin/income-expense/delete/{id}")
    public String deleteTransaction(@PathVariable long id) {
        incomeExpenseRepository.deleteById(id);
        return "redirect:/admin/income-expense";
    }

    @PostMapping("/admin/payment-details")
    public String updatePaymentDetails(@ModelAttribute PaymentDetails details, RedirectAttributes redirectAttributes) {
        details.setId(1L);
        paymentDetailsRepository.save(details);

        redirectAttributes.addFlashAttribute("success_message", "Сохранено!");
        return "redirect:/admin/payment-details";
    }

    @GetMapping("/admin/roles")
    public String showRolesPage(Model model) {
        return "admin_panel/system_settings/roles";
    }

    @PostMapping("/admin/roles")
    public String saveRolesPage(RedirectAttributes redirectAttributes, Model model) {
        redirectAttributes.addFlashAttribute("success", "Сохранено!");
        return "redirect:/admin/roles";
    }

}
