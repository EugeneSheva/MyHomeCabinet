package com.example.myhome.home.controller;

import com.example.myhome.home.model.*;
import com.example.myhome.home.repository.*;
import com.example.myhome.home.service.RoleService;
import com.example.myhome.home.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Controller
@RequiredArgsConstructor
@Log
public class SettingsController {

    private final IncomeExpenseRepository incomeExpenseRepository;
    private final PaymentDetailsRepository paymentDetailsRepository;
    private final PageRoleDisplayRepository pageRoleDisplayRepository;

    private final UserRoleService userRoleService;


    @GetMapping("/admin")
    public String redirectToStatPage() {
        return "redirect:/admin/statistics";
    }

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
                transactions = incomeExpenseRepository.findAll(Sort.by(Sort.Direction.ASC, "incomeExpenseType"));
                model.addAttribute("type", "inc");
            }
            else if(sort.equalsIgnoreCase("inc")) {
                transactions = incomeExpenseRepository.findAll(Sort.by(Sort.Direction.DESC, "incomeExpenseType"));
                model.addAttribute("type", "exp");
            }
        } else {
            transactions = incomeExpenseRepository.findAll();
            model.addAttribute("type", "exp");
        }
        log.info(transactions.toString());
        transactions.forEach(System.out::println);
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
        PageRoleForm pageForm = new PageRoleForm();
        pageForm.setPages(pageRoleDisplayRepository.findAll());
        model.addAttribute("pageForm", pageForm);
        return "admin_panel/system_settings/roles";
    }

    @PostMapping("/admin/roles")
    public String saveRolesPage(@ModelAttribute PageRoleForm pageForm, RedirectAttributes redirectAttributes, Model model) {
        List<PageRoleDisplay> originalList = pageRoleDisplayRepository.findAll();
        List<PageRoleDisplay> pages = pageForm.getPages();
        for(int i = 0; i < pages.size(); i++) {
            pages.get(i).setId((long) i+1);
            pages.get(i).setPage_name(originalList.get(i).getPage_name());
            pages.get(i).setRole_director(true);
            pages.get(i).setCode(originalList.get(i).getCode());
        }
        pageRoleDisplayRepository.saveAll(pages);
        userRoleService.updateRoles(pages);
        redirectAttributes.addFlashAttribute("success", "Сохранено!");
        return "redirect:/admin/roles";
    }

}
