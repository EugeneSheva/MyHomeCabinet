package com.example.myhome.home.controller.admin_panel;

import com.example.myhome.home.model.*;
import com.example.myhome.home.repos.*;
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
import java.util.List;

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

    @GetMapping("/admin/services")
    public String showServicesPage(Model model) {
        model.addAttribute("services", serviceRepository.findAll());
        model.addAttribute("units", unitRepository.findAll());
        return "settings_services";
    }

    @GetMapping("/admin/tariffs")
    public String showTariffsPage(Model model) {
        model.addAttribute("tariffs", tariffRepository.findAll());
        return "settings_tariffs";
    }

    @GetMapping("/admin/admins")
    public String showAdminsPage(Model model) {
        model.addAttribute("admins", adminRepository.findAll());
        return "settings_users";
    }

    @GetMapping("/admin/admins/{id}")
    public String showAdminProfile(@PathVariable long id, Model model) {
        model.addAttribute("admin", adminRepository.findById(id).orElseGet(Admin::new));
        return "admin_profile";
    }

    @GetMapping("/admin/admins/create")
    public String showCreateAdminPage(Model model) {
        model.addAttribute("admin", new Admin());
        return "admin_card";
    }

    @GetMapping("/admin/admins/update/{id}")
    public String showUpdateAdminPage(@PathVariable long id, Model model) {
        model.addAttribute("admin", adminRepository.findById(id).orElseGet(Admin::new));
        return "admin_card";
    }

    @GetMapping("/admin/payment-details")
    public String showPaymentDetailsPage(Model model) {
        model.addAttribute("details", paymentDetailsRepository.findById(1L).orElseGet(PaymentDetails::new));
        return "settings_payment";
    }

    @GetMapping("/admin/income-expense")
    public String showTransactionsPage(Model model) {
        model.addAttribute("transactions", incomeExpenseRepository.findAll());
        return "settings_inc_exp";
    }

    @GetMapping("/admin/income-expense/create")
    public String showCreateTransactionPage(Model model) {
        model.addAttribute("transaction", new IncomeExpenseItems());
        return "transaction_card";
    }

    // ==========================

    @PostMapping("/admin/services")
    public String editServices(@RequestParam String[] units,
                               @RequestParam String[] service_names,
                               @RequestParam String[] service_unit_names,
                               @RequestParam String[] service_unit_ids,
                               @RequestParam boolean[] show_in_meters, RedirectAttributes redirectAttributes) {
        try {
            for(int i = 0; i < service_unit_ids.length; i++) unitRepository.deleteById(Long.parseLong(service_unit_ids[0]));

            for(int i = 0; i < units.length-1; i++) {

                    if(units[i].isEmpty() || units[i].equals("")) continue;
                    Unit u = new Unit();
                    u.setName(units[i]);
                    unitRepository.save(u);

            }


        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("fail", "Что-то уже используется в расчётах");
            return "redirect:/admin/services#tab_serviceunit";
        }

        log.info(Arrays.toString(service_names));
        log.info(Arrays.toString(service_unit_names));
        log.info(Arrays.toString(show_in_meters));
        log.info(String.valueOf(service_names.length));
        log.info(String.valueOf(service_unit_names.length));
        log.info(String.valueOf(show_in_meters.length));

        // удаляются все услуги из БД , потом добавляются заново - не совсем удобно

        serviceRepository.deleteAll();

        for (int i = 0; i < service_names.length-1; i++) {
            if(service_names[i].isEmpty() || service_names[i].equals("")) continue;

            Service service =
                    new Service(service_names[i],
                            show_in_meters[i],
                            unitRepository.findByName(service_unit_names[i]).orElseThrow());
            serviceRepository.save(service);
        }
        return "redirect:/admin/services";
    }

    @PostMapping("/admin/income-expense/create")
    public String createTransaction(@RequestParam String name, @RequestParam String type) {
        if(!incomeExpenseRepository.existsByName(name))
            incomeExpenseRepository.save(new IncomeExpenseItems(name, IncomeExpenseType.valueOf(type)));
        return "redirect:/admin/income-expense";
    }

    @PostMapping("/admin/income-expense/update/{id}")
    public String updateTransaction(@PathVariable long id, @RequestParam String name, @RequestParam String type) {
        IncomeExpenseItems transaction = incomeExpenseRepository.findById(id).orElseThrow();
        transaction.setName(name);
        transaction.setIncomeExpenseType(IncomeExpenseType.valueOf(type));
        incomeExpenseRepository.save(transaction);
        return "redirect:/admin/income-expense";
    }

    @GetMapping("/admin/income-expense/update/{id}")
    public String showUpdateTransactionPage(@PathVariable long id, Model model) {
        model.addAttribute("transaction", incomeExpenseRepository.findById(id).orElseThrow());
        return "transaction_card";
    }

    @GetMapping("/admin/income-expense/delete/{id}")
    public String deleteTransaction(@PathVariable long id) {
        incomeExpenseRepository.deleteById(id);
        return "redirect:/admin/income-expense";
    }

    @GetMapping("/admin/tariffs/create")
    public String showCreateTariffCard(Model model){
        model.addAttribute("tariff", new Tariff());
        model.addAttribute("services", serviceRepository.findAll());
        return "tariff_card";
    }

    @PostMapping("/admin/tariffs/create")
    public String createTariff(@RequestParam String name,
                               @RequestParam String description,
                               @RequestParam String[] service_names,
                               @RequestParam String[] prices) {
        Tariff tariff = new Tariff();
        tariff.setName(name);
        tariff.setDescription(description);
        tariff.setDate(LocalDateTime.now());
        tariff.setTariffComponentsList(new ArrayList<>());

        for (int i = 1; i < service_names.length; i++) {
            log.info(service_names[i]);
            log.info(prices[i]);
            tariff.getTariffComponentsList().add(new TariffComponents(service_names[i], Double.parseDouble(prices[i])));
        }

        tariffRepository.save(tariff);

        return "redirect:/admin/tariffs";
    }

    @GetMapping("/admin/tariffs/update/{id}")
    public String showUpdateTariffPage(@PathVariable long id, Model model) {
        model.addAttribute("tariff", tariffRepository.findById(id).orElseGet(Tariff::new));
        model.addAttribute("services", serviceRepository.findAll());
        return "tariff_card";
    }

    @PostMapping("/admin/tariffs/update/{id}")
    public String updateTariff(@PathVariable long id,
                               @RequestParam String name,
                               @RequestParam String description,
                               @RequestParam String[] service_names,
                               @RequestParam String[] prices) {
        Tariff tariff = new Tariff();
        tariff.setId(id);
        tariff.setName(name);
        tariff.setDescription(description);
        tariff.setDate(LocalDateTime.now());
        tariff.setTariffComponentsList(new ArrayList<>());

        for (int i = 1; i < service_names.length; i++) {
            log.info(service_names[i]);
            log.info(prices[i]);
            tariff.getTariffComponentsList().add(new TariffComponents(service_names[i], Double.parseDouble(prices[i])));
        }

        tariffRepository.save(tariff);

        return "redirect:/admin/tariffs";
    }

    @GetMapping("/admin/tariffs/delete/{id}")
    public String deleteTariff(@PathVariable long id) {
        tariffRepository.deleteById(id);
        return "redirect:/admin/tariffs";
    }

    @PostMapping("/admin/payment-details")
    public String updatePaymentDetails(@ModelAttribute PaymentDetails details, RedirectAttributes redirectAttributes) {
        details.setId(1L);
        paymentDetailsRepository.save(details);

        redirectAttributes.addFlashAttribute("success_message", "Сохранено!");
        return "redirect:/admin/payment-details";
    }

    // АДМИНЫ - ДОБАВИТЬ ВАЛИДАЦИЮ

    @PostMapping("/admin/admins/create")
    public String createAdmin(@ModelAttribute Admin admin) {
        admin.setRole(UserRole.ADMIN);
        adminRepository.save(admin);
        return "redirect:/admin/admins";
    }

    @PostMapping("/admin/admins/update/{id}")
    public String updateAdmin(@PathVariable long id, @ModelAttribute Admin admin) {
        admin.setId(id);
        admin.setRole(UserRole.ADMIN); // пока не добавил выбор ролей , всем ставится админ
        adminRepository.save(admin);
        return "redirect:/admin/admins";
    }

    @GetMapping("/admin/admins/delete/{id}")
    public String deleteAdmin(@PathVariable long id) {
        adminRepository.deleteById(id);
        return "redirect:/admin/admins";
    }

    @GetMapping("/admin/admins/invite/{id}")
    public void inviteAdmin(@PathVariable long id) {}

    @GetMapping("/admin/roles")
    public String showRolesPage(Model model) {
        return "admin_panel/roles";
    }

    @PostMapping("/admin/roles")
    public String saveRolesPage(RedirectAttributes redirectAttributes, Model model) {
        redirectAttributes.addFlashAttribute("success", "Сохранено!");
        return "redirect:/admin/roles";
    }

}
