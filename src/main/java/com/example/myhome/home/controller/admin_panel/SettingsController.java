package com.example.myhome.home.controller.admin_panel;

import com.example.myhome.home.model.*;
import com.example.myhome.home.repository.*;
import com.example.myhome.home.service.AdminService;
import com.example.myhome.home.service.ServiceService;
import com.example.myhome.home.service.TariffService;
import com.example.myhome.util.UserRole;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
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
    private IncomeExpenseRepository incomeExpenseRepository;

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;


    @GetMapping("/admin/payment-details")
    public String showPaymentDetailsPage(Model model) {
        model.addAttribute("paymentDetails", paymentDetailsRepository.findById(1L).orElseGet(PaymentDetails::new));
        return "admin_panel/system_settings/settings_payment";
    }

    @GetMapping("/admin/income-expense")
    public String showTransactionsPage(@RequestParam(required = false) String sort, Model model) {
        List<IncomeExpenseItems> transactions = new ArrayList<>();
        if(sort != null) {
            if(sort.equalsIgnoreCase("exp")) {
                transactions = incomeExpenseRepository.findAllByOrderByIncomeExpenseTypeAsc();
                model.addAttribute("type", "inc");
            }
            else if(sort.equalsIgnoreCase("inc")) {
                transactions = incomeExpenseRepository.findAllByOrderByIncomeExpenseTypeDesc();
                model.addAttribute("type", "exp");
            }
        } else {
            transactions = incomeExpenseRepository.findAll();
            model.addAttribute("type", "exp");
        }
        model.addAttribute("transactions", transactions);
        return "admin_panel/system_settings/settings_inc_exp";
    }

    @GetMapping("/admin/income-expense/create")
    public String showCreateTransactionPage(Model model) {
        model.addAttribute("incomeExpenseItems", new IncomeExpenseItems());
        return "admin_panel/system_settings/transaction_card";
    }

    @PostMapping("/admin/income-expense/create")
    public String createTransaction(@Valid @ModelAttribute IncomeExpenseItems item, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            log.info("errors found");
            log.info(bindingResult.getObjectName());
            log.info(bindingResult.getAllErrors().toString());
            return "admin_panel/system_settings/transaction_card";
        }
        if(!incomeExpenseRepository.existsByName(item.getName()))
            incomeExpenseRepository.save(new IncomeExpenseItems(item.getName(), item.getIncomeExpenseType()));
        return "redirect:/admin/income-expense";
    }

    @GetMapping("/admin/income-expense/update/{id}")
    public String showUpdateTransactionPage(@PathVariable long id, Model model) {
        model.addAttribute("incomeExpenseItems", incomeExpenseRepository.findById(id).orElseThrow());
        return "admin_panel/system_settings/transaction_card";
    }

    @PostMapping("/admin/income-expense/update/{id}")
    public String updateTransaction(@Valid @ModelAttribute IncomeExpenseItems item, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            log.info("errors found");
            log.info(bindingResult.getObjectName());
            log.info(bindingResult.getAllErrors().toString());
            return "admin_panel/system_settings/transaction_card";
        }
        incomeExpenseRepository.save(item);
        return "redirect:/admin/income-expense";
    }

    @GetMapping("/admin/income-expense/delete/{id}")
    public String deleteTransaction(@PathVariable long id) {
        incomeExpenseRepository.deleteById(id);
        return "redirect:/admin/income-expense";
    }

    @PostMapping("/admin/payment-details")
    public String updatePaymentDetails(@Valid @ModelAttribute PaymentDetails details, BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            log.info("errors found");
            log.info(bindingResult.getObjectName());
            log.info(bindingResult.getAllErrors().toString());
            return "admin_panel/system_settings/settings_payment";
        }

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
