package com.example.myhome.home.controller;

import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.model.ApartmentAccount;
import com.example.myhome.home.repository.AccountRepository;
import com.example.myhome.home.repository.ApartmentRepository;
import com.example.myhome.home.repository.BuildingRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/accounts")
@Log
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    // показать все счета
    @GetMapping
    public String showAccountsPage(Model model) {
        model.addAttribute("accounts", accountRepository.findAll());
        return "admin_panel/accounts/accounts";
    }

    // показать профиль лицевого счёта
    @GetMapping("/{id}")
    public String showAccountInfoPage(@PathVariable long id, Model model) {
        model.addAttribute("account", accountRepository.findById(id).orElseThrow());
        return "admin_panel/accounts/account_profile";
    }

    // открытие страницы создания лицевого счета
    @GetMapping("/create")
    public String showCreateAccountPage(Model model) {
        Optional<ApartmentAccount> accWithBiggestID = accountRepository.findFirstByOrderByIdDesc();
        long id;
        if(accWithBiggestID.isEmpty()) id = 1L;
        else id = accWithBiggestID.get().getId()+1;
        model.addAttribute("account", new ApartmentAccount());
        model.addAttribute("id", id);
        model.addAttribute("buildings", buildingRepository.findAll());
        return "admin_panel/accounts/account_card";
    }

    // создание лицевого счета
    @PostMapping("/create")
    public String createAccount(@ModelAttribute ApartmentAccount account) {

        // взаимные ссылки не устанавливаются - Account получает apartment_id
        // как главный объект в отношении OneToOne, а вот Apartment account_id не получает ,
        // если не делать выкрутасы, как указано ниже

//        ApartmentAccount savedAccount = accountRepository.save(account);
//        Apartment apartment = apartmentRepository.findById(savedAccount.getApartment().getId()).orElseThrow();
//        apartment.setAccount(savedAccount);
//        apartmentRepository.save(apartment);

        accountRepository.save(account);

        return "redirect:/admin/accounts";
    }

    @GetMapping("/update/{id}")
    public String showUpdateAccountPage(@PathVariable long id, Model model) {
        model.addAttribute("account", accountRepository.findById(id).orElseThrow());
        model.addAttribute("id", id);
        model.addAttribute("buildings", buildingRepository.findAll());
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

        accountRepository.save(account);

        return "redirect:/admin/accounts";
    }

    @GetMapping("/delete/{id}")
    public String deleteAccount(@PathVariable long id) {
        accountRepository.deleteById(id);
        return "redirect:/admin/accounts";
    }

    @GetMapping("/get-flat-account")
    public @ResponseBody String getAccountNumberFromFlat(@RequestParam long flat_id) {
        return String.format("%010d", accountRepository.findByApartmentId(flat_id).orElseThrow().getId());
    }

}
