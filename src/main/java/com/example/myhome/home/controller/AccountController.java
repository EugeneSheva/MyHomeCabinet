package com.example.myhome.home.controller;

import com.example.myhome.home.mapper.AccountDTOMapper;
import com.example.myhome.home.model.ApartmentAccount;
import com.example.myhome.home.dto.ApartmentAccountDTO;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.service.AccountService;
import com.example.myhome.home.service.BuildingService;
import com.example.myhome.home.service.CashBoxService;
import com.example.myhome.home.service.OwnerService;
import com.example.myhome.home.service.impl.AccountServiceImpl;
import com.example.myhome.home.validator.AccountValidator;
import com.example.myhome.util.MappingUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@Controller
@RequestMapping("/admin/accounts")
@Log
public class AccountController {

    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private CashBoxService cashBoxService;

    @Autowired
    private BuildingService buildingService;

    @Autowired private OwnerService ownerService;

    @Autowired private AccountValidator validator;

    @Autowired private AccountDTOMapper mapper;

    // показать все счета
    @GetMapping
    public String showAccountsPage(Model model,
                                   FilterForm filterForm) throws IllegalAccessException {

        model.addAttribute("cashbox_balance", cashBoxService.calculateBalance());
        model.addAttribute("account_balance", accountService.getSumOfAccountBalances());
        model.addAttribute("account_debt", accountService.getSumOfAccountDebts());
        model.addAttribute("owners", ownerService.findAllDTO());
        model.addAttribute("buildings", buildingService.findAllDTO());

        if(filterForm.getBuilding() != null)
            model.addAttribute("sections", buildingService.findById(filterForm.getBuilding()).getSections());

        model.addAttribute("filter_form", filterForm);
        model.addAttribute("page", filterForm.getPage());

        return "admin_panel/accounts/accounts";
    }

    // показать профиль лицевого счёта
    @GetMapping("/{id}")
    public String showAccountInfoPage(@PathVariable long id, Model model) {
        model.addAttribute("account", accountService.findAccountDTOById(id));
        return "admin_panel/accounts/account_profile";
    }

    // открытие страницы создания лицевого счета
    @GetMapping("/create")
    public String showCreateAccountPage(Model model) {
        ApartmentAccountDTO dto = new ApartmentAccountDTO();

        model.addAttribute("apartmentAccountDTO", dto);
        model.addAttribute("id", accountService.getMaxAccountId()+1);
        model.addAttribute("buildings", buildingService.findAll());

        return "admin_panel/accounts/account_card";
    }

    // создание лицевого счета
    @PostMapping("/create")
    public String createAccount(@ModelAttribute ApartmentAccountDTO apartmentAccountDTO,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        log.info(apartmentAccountDTO.toString());
        validator.validate(apartmentAccountDTO, bindingResult);

        if(bindingResult.hasErrors()) {
            log.info("Errors found");
            log.info(bindingResult.getAllErrors().toString());

            ApartmentAccountDTO dto = (ApartmentAccountDTO) bindingResult.getTarget();

            if(dto != null && dto.getBuilding() != null && dto.getBuilding().getId() != null) {
                dto.setBuilding(buildingService.findBuildingDTObyId(dto.getBuilding().getId()));
            }

            model.addAttribute("apartmentAccountDTO", dto);
            model.addAttribute("id", accountService.getMaxAccountId()+1);
            model.addAttribute("buildings", buildingService.findAllDTO());

            return "admin_panel/accounts/account_card";
        } else {
            accountService.saveAccount(apartmentAccountDTO);
            return "redirect:/admin/accounts";
        }


    }

    @GetMapping("/update/{id}")
    public String showUpdateAccountPage(@PathVariable long id, Model model) {

        model.addAttribute("apartmentAccountDTO", accountService.findAccountDTOById(id));
        model.addAttribute("id", id);
        model.addAttribute("buildings", buildingService.findAllDTO());

        return "admin_panel/accounts/account_card";
    }

    @PostMapping("/update/{id}")
    public String updateAccount(@PathVariable long id,
                                @ModelAttribute ApartmentAccountDTO apartmentAccountDTO,
                                BindingResult bindingResult,
                                Model model) {

        validator.validate(apartmentAccountDTO, bindingResult);
        if(bindingResult.hasErrors()) {
            log.info("Errors found");
            log.info(bindingResult.getAllErrors().toString());
            model.addAttribute("buildings", buildingService.findAllDTO());
            log.info(buildingService.findAllDTO().toString());
            return "admin_panel/accounts/account_card";
        }
        accountService.saveAccount(apartmentAccountDTO);

        return "redirect:/admin/accounts";
    }

    @GetMapping("/delete/{id}")
    public String deleteAccount(@PathVariable long id) {
        accountService.deleteAccountById(id);
        return "redirect:/admin/accounts";
    }

    @GetMapping("/get-flat-account")
    public @ResponseBody Long getAccountNumberFromFlat(@RequestParam long flat_id) {
        return accountService.getAccountNumberFromFlat(flat_id).getId();
    }

    @GetMapping("/get-accounts")
    public @ResponseBody Page<ApartmentAccountDTO> getAccounts(@RequestParam Integer page,
                                                               @RequestParam Integer size,
                                                               @RequestParam String filters) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FilterForm form = mapper.readValue(filters, FilterForm.class);
        return accountService.findAllAccountsByFiltersAndPage(form, PageRequest.of(page-1, size));
    }

    @ModelAttribute
    public void addAttributes(Model model) {
    }

}
