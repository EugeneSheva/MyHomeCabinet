package com.example.myhome.home.controller;
import com.example.myhome.home.model.*;
import com.example.myhome.home.repository.IncomeExpenseRepository;
import com.example.myhome.home.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/cashbox")
public class CashBoxController {

    private final CashBoxService cashBoxService;
    private final OwnerService ownerService;
    private final AdminService adminService;
    private final ApartmentAccountService apartmentAccountService;
    private final IncomeExpenseItemService incomeExpenseItemService;
    private final IncomeExpenseRepository incomeExpenseRepository;

    @GetMapping("/")
    public String getCashBox(Model model) {
        List<CashBox> cashBoxList = cashBoxService.findAll();
        model.addAttribute("cashBoxList", cashBoxList);
        return "admin_panel/cashboxes";
    }

    @GetMapping("/{id}")
    public String getCashBox(@PathVariable("id") Long id, Model model) {
        CashBox cashBox = cashBoxService.findById(id);
        model.addAttribute("cashBoxItem", cashBox);
        return "admin_panel/cashbox";
    }

    @GetMapping("/newIncome")
    public String createCashBoxIn(Model model) {
        List<IncomeExpenseItems>incomeItemsList=incomeExpenseRepository.findAllByIncomeExpenseType(IncomeExpenseType.INCOME);
        model.addAttribute("incomeItemsList", incomeItemsList);        ;

        List<AdminDTO>adminDTOList = adminService.findAllDTO();
        model.addAttribute("admins", adminDTOList);

        List<OwnerDTO> ownerDTOList = ownerService.findAllDTO();
        model.addAttribute("owners", ownerDTOList);

        List<ApartmentAccountDTO> apartmentAccountDTOS = apartmentAccountService.findDtoApartmentAccounts();
        model.addAttribute("accounts", apartmentAccountDTOS);

        CashBox cashBox = new CashBox();
        cashBox.setIncomeExpenseType(IncomeExpenseType.INCOME);
        model.addAttribute("cashBoxItem", cashBox);
        return "admin_panel/cashbox_edit";
    }
    @GetMapping("/newExpense")
    public String createCashBoxEx(Model model) {
        List<IncomeExpenseItems>expenseItemsList=incomeExpenseRepository.findAllByIncomeExpenseType(IncomeExpenseType.EXPENSE);
        model.addAttribute("expenseItemsList", expenseItemsList);

        List<AdminDTO>adminDTOList = adminService.findAllDTO();
        model.addAttribute("admins", adminDTOList);

        List<OwnerDTO> ownerDTOList = ownerService.findAllDTO();
        model.addAttribute("owners", ownerDTOList);

        List<ApartmentAccountDTO> apartmentAccountDTOS = apartmentAccountService.findDtoApartmentAccounts();
        model.addAttribute("accounts", apartmentAccountDTOS);

        CashBox cashBox = new CashBox();
        cashBox.setIncomeExpenseType(IncomeExpenseType.EXPENSE);
        model.addAttribute("cashBoxItem", cashBox);
        return "admin_panel/cashbox_edit";
    }
    @GetMapping("edit/{id}")
    public String editeCashBox(@PathVariable("id") Long id, Model model) {
        CashBox cashBox = cashBoxService.findById(id);
        model.addAttribute("cashBoxItem", cashBox);
        return "admin_panel/cashbox_edit";
    }

    @PostMapping("/save")
    public String saveCashBox(@ModelAttribute("cashbox") CashBox cashbox, @RequestParam(name = "owner", defaultValue = "0") Long ownerId,
                              @RequestParam(name = "admin", defaultValue = "0") Long adminId, @RequestParam(name = "account", defaultValue = "0") Long accountId,
                              @RequestParam(name = "amount", defaultValue = "0D") Double amount, @RequestParam(name = "incomeExpenseItem", defaultValue = "0") Long incomeExpenseItemId) throws IOException {
        System.out.println("incomeExpenseItem "+ incomeExpenseItemId);
        if(cashbox.getIncomeExpenseType()==IncomeExpenseType.EXPENSE) {
            if(amount>0) cashbox.setAmount(amount*(-1));
        } else {
            cashbox.setOwner(ownerService.findById(ownerId));
            cashbox.setApartmentAccount(apartmentAccountService.findById(accountId));
        }
        cashbox.setIncomeExpenseItems(incomeExpenseItemService.findById(incomeExpenseItemId));
        cashbox.setManager(adminService.findAdminById(adminId));
        cashBoxService.save(cashbox);
        return "redirect:/cashbox/";
    }
//
    @GetMapping("/delete/{id}")
    public String dellete(@PathVariable("id") Long id) {
        cashBoxService.deleteById(id);
        return "redirect:/cashbox/";
    }
}
