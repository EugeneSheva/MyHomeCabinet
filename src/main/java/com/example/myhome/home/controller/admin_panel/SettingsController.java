package com.example.myhome.home.controller.admin_panel;

import com.example.myhome.home.model.IncomeExpenseItems;
import com.example.myhome.home.model.IncomeExpenseType;
import com.example.myhome.home.model.Service;
import com.example.myhome.home.model.Unit;
import com.example.myhome.home.repos.IncomeExpenseRepository;
import com.example.myhome.home.repos.ServiceRepository;
import com.example.myhome.home.repos.UnitRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/admin/services")
    public String showEditHomePage(Model model) {
        model.addAttribute("services", serviceRepository.findAll());
        model.addAttribute("units", unitRepository.findAll());
        return "settings_services";
    }

    @GetMapping("/admin/tariffs")
    public String showEditAboutPage(Model model) {return "settings_tariffs";}

    @GetMapping("/admin/users")
    public String showEditServicesPage(Model model) {return "settings_users";}

    @GetMapping("/admin/payment-details")
    public String showEditTariffsPage(Model model) {return "settings_payment";}

    @GetMapping("/admin/income-expense")
    public String showEditContactsPage(Model model) {
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
                               @RequestParam boolean[] show_in_meters) {
        for(String unit : units) {
            if(!unit.equals("") && !unitRepository.existsByName(unit)) {
                Unit u = new Unit();
                u.setName(unit);
                unitRepository.save(u);
            }
        }

        // удаляются все услуги из БД , потом добавляются заново - не совсем удобно

        serviceRepository.deleteAll();

        for (int i = 0; i < service_names.length; i++) {
            if(service_names[i].isEmpty()) continue;
            log.info(service_unit_names[i]);

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

}
