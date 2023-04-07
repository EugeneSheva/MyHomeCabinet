package com.example.myhome.home.controller;
import com.example.myhome.home.model.*;
import com.example.myhome.home.repository.AccountRepository;
import com.example.myhome.home.repository.CashBoxRepository;
import com.example.myhome.home.repository.IncomeExpenseRepository;
import com.example.myhome.home.service.*;
import com.example.myhome.home.validator.CashBoxtValidator;
import com.example.myhome.util.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/cashbox")
public class CashBoxController {

    private final CashBoxService cashBoxService;
    private final OwnerService ownerService;
    private final AdminService adminService;
    private final ApartmentAccountService apartmentAccountService;
    private final IncomeExpenseItemService incomeExpenseItemService;
    private final IncomeExpenseRepository incomeExpenseRepository;
    private final AccountRepository accountRepository;
    private final CashBoxRepository cashBoxRepository;

    private final CashBoxtValidator cashBoxtValidator;

    @GetMapping("/")
    public String getCashBox(Model model) {
        List<CashBox> cashBoxList = cashBoxService.findAll();
        model.addAttribute("cashBoxList", cashBoxList);
        model.addAttribute("cashBoxSum", cashBoxRepository.sumAmount());
        model.addAttribute("accountBalance", accountRepository.getSumOfAccountBalances());
        model.addAttribute("sumDebt", accountRepository.getSumOfAccountDebts());
        return "admin_panel/cash_box/cashboxes";
    }

    @GetMapping("/{id}")
    public String getCashBox(@PathVariable("id") Long id, Model model) {
        CashBox cashBox = cashBoxService.findById(id);
        model.addAttribute("cashBoxItem", cashBox);
        return "admin_panel/cash_box/cashbox";
    }

    @GetMapping("/newIncome")
    public String createCashBoxIn(Model model) {
        List<IncomeExpenseItems>incomeItemsList=incomeExpenseRepository.findAllByIncomeExpenseType(IncomeExpenseType.INCOME);
        model.addAttribute("incomeItemsList", incomeItemsList);

        List<AdminDTO>adminDTOList = adminService.findAllDTO();

        // получение только юзеров с ролями "админ", "директор", "бухгалтер", без сантехников и т.д.
        adminDTOList = adminDTOList.stream()
                .filter(admin -> admin.getRole() == UserRole.ADMIN ||
                        admin.getRole() == UserRole.DIRECTOR ||
                        admin.getRole() == UserRole.MANAGER ||
                        admin.getRole() == UserRole.ACCOUNTANT)
                .collect(Collectors.toList());
        model.addAttribute("admins", adminDTOList);

        List<OwnerDTO> ownerDTOList = ownerService.findAllDTO();
        model.addAttribute("owners", ownerDTOList);

        List<ApartmentAccountDTO> apartmentAccountDTOS = apartmentAccountService.findDtoApartmentAccounts();
        model.addAttribute("accounts", apartmentAccountDTOS);

        CashBox cashBox = new CashBox();
        cashBox.setIncomeExpenseType(IncomeExpenseType.INCOME);
        model.addAttribute("cashBoxItem", cashBox);
        return "admin_panel/cash_box/cashbox_edit";
    }
    @GetMapping("/newExpense")
    public String createCashBoxEx(Model model) {
        List<IncomeExpenseItems>expenseItemsList=incomeExpenseRepository.findAllByIncomeExpenseType(IncomeExpenseType.EXPENSE);
        model.addAttribute("expenseItemsList", expenseItemsList);

        List<AdminDTO>adminDTOList = adminService.findAllDTO();
        model.addAttribute("admins", adminDTOList);

        CashBox cashBox = new CashBox();
        cashBox.setIncomeExpenseType(IncomeExpenseType.EXPENSE);
        model.addAttribute("cashBoxItem", cashBox);
        return "admin_panel/cash_box/cashbox_edit";
    }
    @GetMapping("edit/{id}")
    public String editeCashBox(@PathVariable("id") Long id, Model model) {
        CashBox cashBox = cashBoxService.findById(id);
        model.addAttribute("cashBoxItem", cashBox);
        return "admin_panel/cash_box/cashbox_edit";
    }

    @GetMapping("copy/{id}")
    public String copyCashBox(@PathVariable("id") Long id, Model model) {
        CashBox cashBox = cashBoxService.findById(id);
        cashBox.setId(null);
        model.addAttribute("cashBoxItem", cashBox);
        return "admin_panel/cash_box/cashbox_edit";
    }

    @PostMapping("/save")
    public String saveCashBox(@Valid @ModelAttribute("cashBoxItem") CashBox cashbox, BindingResult bindingResult, @RequestParam(name = "id", defaultValue = "0") Long id, @RequestParam(name = "owner", defaultValue = "0") Long ownerId,
                              @RequestParam(name = "admin", defaultValue = "0") Long adminId, @RequestParam(name = "account", defaultValue = "0") Long accountId,
                              @RequestParam(name = "amount", defaultValue = "0D") Double amount, @RequestParam(name = "incomeExpenseItem", defaultValue = "0") Long incomeExpenseItemId, Model model) throws IOException {
        cashBoxtValidator.validate(cashbox, bindingResult);
        if (bindingResult.hasErrors()) {
            if(cashbox.getIncomeExpenseType().equals(IncomeExpenseType.INCOME)){
                List<IncomeExpenseItems>incomeItemsList=incomeExpenseRepository.findAllByIncomeExpenseType(IncomeExpenseType.INCOME);
                model.addAttribute("incomeItemsList", incomeItemsList);

                List<OwnerDTO> ownerDTOList = ownerService.findAllDTO();
                model.addAttribute("owners", ownerDTOList);

                List<ApartmentAccountDTO> apartmentAccountDTOS = apartmentAccountService.findDtoApartmentAccounts();
                model.addAttribute("accounts", apartmentAccountDTOS);
            } else if (cashbox.getIncomeExpenseType().equals(IncomeExpenseType.EXPENSE)){
                List<IncomeExpenseItems>expenseItemsList=incomeExpenseRepository.findAllByIncomeExpenseType(IncomeExpenseType.EXPENSE);
                model.addAttribute("expenseItemsList", expenseItemsList);
            }
            List<AdminDTO>adminDTOList = adminService.findAllDTO();
            model.addAttribute("admins", adminDTOList);

            return "admin_panel/cash_box/cashbox_edit";
        } else {
            if (cashbox.getIncomeExpenseType() == IncomeExpenseType.EXPENSE) {
                if (amount > 0) cashbox.setAmount(amount * (-1));
            } else {
                if (id>0) cashbox.setId(id);
                cashbox.setOwner(ownerService.findById(ownerId));
                cashbox.setApartmentAccount(apartmentAccountService.findById(accountId));
            }
            if (id>0) cashbox.setId(id);
            cashbox.setIncomeExpenseItems(incomeExpenseItemService.findById(incomeExpenseItemId));
            cashbox.setManager(adminService.findAdminById(adminId));
            cashBoxService.save(cashbox);
            return "redirect:/admin/cashbox/";
        }
    }
//
    @GetMapping("/delete/{id}")
    public String dellete(@PathVariable("id") Long id) {
        cashBoxService.deleteById(id);
        return "redirect:/admin/cashbox/";
    }
}
