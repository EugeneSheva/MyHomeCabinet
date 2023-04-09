package com.example.myhome.home.controller;

import com.example.myhome.home.model.ApartmentAccount;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.AccountRepository;
import com.example.myhome.home.repository.ApartmentRepository;
import com.example.myhome.home.repository.BuildingRepository;
import com.example.myhome.home.service.AccountService;
import com.example.myhome.home.service.BuildingService;
import com.example.myhome.home.service.CashBoxService;
import com.example.myhome.home.service.OwnerService;
import com.example.myhome.home.validator.AccountValidator;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/accounts")
@Log
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CashBoxService cashBoxService;

    @Autowired
    private BuildingService buildingService;

    @Autowired private OwnerService ownerService;

    @Autowired private AccountValidator validator;

    // показать все счета
    @GetMapping
    public String showAccountsPage(Model model,
                                   FilterForm filterForm) throws IllegalAccessException {

        List<ApartmentAccount> accountList;

        if(!filterForm.filtersPresent()) accountList = accountService.findAll();
        else accountList = accountService.findAllBySpecification(filterForm);

        model.addAttribute("accounts", accountList);
        model.addAttribute("cashbox_balance", cashBoxService.calculateBalance());
        model.addAttribute("account_balance", accountService.getSumOfAccountBalances());
        model.addAttribute("account_debt", accountService.getSumOfAccountDebts());

        model.addAttribute("owners", ownerService.findAllDTO());
        model.addAttribute("buildings", buildingService.findAllDTO());

        if(filterForm.getBuilding() != null)
            model.addAttribute("sections", buildingService.findById(filterForm.getBuilding()).getSections());

        model.addAttribute("filter_form", filterForm);


        return "admin_panel/accounts/accounts";
    }

    // показать профиль лицевого счёта
    @GetMapping("/{id}")
    public String showAccountInfoPage(@PathVariable long id, Model model) {
        model.addAttribute("account", accountService.getAccountById(id));
        return "admin_panel/accounts/account_profile";
    }

    // открытие страницы создания лицевого счета
    @GetMapping("/create")
    public String showCreateAccountPage(Model model) {
        model.addAttribute("apartmentAccount", new ApartmentAccount());
        return "admin_panel/accounts/account_card";
    }

    // создание лицевого счета
    @PostMapping("/create")
    public String createAccount(@ModelAttribute ApartmentAccount account,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {

        log.info(account.toString());
        validator.validate(account, bindingResult);
        log.info("account apartment id" + account.getApartment().getId());

        if(bindingResult.hasErrors()) {
            log.info("Errors found");
            log.info(bindingResult.getAllErrors().toString());
            return "admin_panel/accounts/account_card";
        } else {
            accountService.save(account);
            return "redirect:/admin/accounts";
        }


    }

    @GetMapping("/update/{id}")
    public String showUpdateAccountPage(@PathVariable long id, Model model) {
        model.addAttribute("apartmentAccount", accountService.getAccountById(id));
        model.addAttribute("id", id);
        return "admin_panel/accounts/account_card";
    }

    @PostMapping("/update/{id}")
    public String updateAccount(@PathVariable long id, @ModelAttribute ApartmentAccount account, BindingResult bindingResult) {
        validator.validate(account, bindingResult);
        if(bindingResult.hasErrors()) {
            log.info("errors found");
            log.info(bindingResult.getAllErrors().toString());
            return "admin_panel/accounts/account_card";
        }
        accountService.save(account);

        return "redirect:/admin/accounts";
    }

    @GetMapping("/delete/{id}")
    public String deleteAccount(@PathVariable long id) {
        accountService.deleteAccountById(id);
        return "redirect:/admin/accounts";
    }

    @GetMapping("/get-flat-account")
    public @ResponseBody String getAccountNumberFromFlat(@RequestParam long flat_id) {
        return String.format("%010d", accountService.getAccountNumberFromFlat(flat_id).getId());
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("id", accountService.getMaxId());
        model.addAttribute("buildings", buildingService.findAll());
    }

}
