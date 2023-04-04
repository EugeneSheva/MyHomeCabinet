package com.example.myhome.home.controller;

import com.example.myhome.home.model.ApartmentAccount;
import com.example.myhome.home.repository.AccountRepository;
import com.example.myhome.home.repository.ApartmentRepository;
import com.example.myhome.home.repository.BuildingRepository;
import com.example.myhome.home.service.AccountService;
import com.example.myhome.home.service.BuildingService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin/accounts")
@Log
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private BuildingService buildingService;

    // показать все счета
    @GetMapping
    public String showAccountsPage(Model model) {
        model.addAttribute("accounts", accountService.findAll());
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
        ApartmentAccount accWithBiggestID = accountService.getAccountWithBiggestId();
        long id = accWithBiggestID.getId()+1;
        model.addAttribute("account", new ApartmentAccount());
        model.addAttribute("id", id);
        model.addAttribute("buildings", buildingService.findAll());
        return "admin_panel/accounts/account_card";
    }

    // создание лицевого счета
    @PostMapping("/create")
    public String createAccount(@ModelAttribute ApartmentAccount account,
                                RedirectAttributes redirectAttributes) {

        // взаимные ссылки не устанавливаются - Account получает apartment_id
        // как главный объект в отношении OneToOne, а вот Apartment account_id не получает ,
        // если не делать выкрутасы, как указано ниже

//        ApartmentAccount savedAccount = accountRepository.save(account);
//        Apartment apartment = apartmentRepository.findById(savedAccount.getApartment().getId()).orElseThrow();
//        apartment.setAccount(savedAccount);
//        apartmentRepository.save(apartment);

        if(accountService.apartmentHasAccount(account.getApartment().getId())) {
            redirectAttributes.addFlashAttribute("fail", "К этой квартире уже привязан лицевой счёт!");
            return "redirect:/admin/accounts/create";
        } else {
            accountService.save(account);
            return "redirect:/admin/accounts";
        }


    }

    @GetMapping("/update/{id}")
    public String showUpdateAccountPage(@PathVariable long id, Model model) {
        model.addAttribute("account", accountService.getAccountById(id));
        model.addAttribute("id", id);
        model.addAttribute("buildings", buildingService.findAll());
        return "admin_panel/accounts/account_card";
    }

    @PostMapping("/update/{id}")
    public String updateAccount(@PathVariable long id, @ModelAttribute ApartmentAccount account) {
//        log.info("OLD APARTMENT ID " + account.getApartment().getId());
//        Apartment old_apartment = apartmentRepository.findById(account.getApartment().getId()).orElseThrow();
//        old_apartment.setAccount(null);
//        apartmentRepository.save(old_apartment);
//
//        ApartmentAccount savedAccount = accountRepository.save(account);
//        Apartment apartment = apartmentRepository.findById(savedAccount.getApartment().getId()).orElseThrow();
//        apartment.setAccount(savedAccount);
//        apartmentRepository.save(apartment);

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

}
