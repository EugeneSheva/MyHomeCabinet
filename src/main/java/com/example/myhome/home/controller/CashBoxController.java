package com.example.myhome.home.controller;
import com.example.myhome.home.controller.socket.WebsocketController;
import com.example.myhome.home.dto.AdminDTO;
import com.example.myhome.home.dto.ApartmentAccountDTO;
import com.example.myhome.home.dto.CashBoxDTO;
import com.example.myhome.home.dto.OwnerDTO;
import com.example.myhome.home.model.*;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.AccountRepository;
import com.example.myhome.home.repository.CashBoxRepository;
import com.example.myhome.home.repository.IncomeExpenseRepository;
import com.example.myhome.home.repository.OwnerRepository;
import com.example.myhome.home.service.*;
import com.example.myhome.home.service.impl.AccountServiceImpl;
import com.example.myhome.home.service.impl.AdminServiceImpl;
import com.example.myhome.home.service.impl.IncomeExpenseItemServiceImpl;
import com.example.myhome.home.validator.CashBoxtValidator;
import com.example.myhome.util.MappingUtils;
import com.example.myhome.util.UserRole;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/cashbox")
@Transactional
public class CashBoxController {

    private final CashBoxService cashBoxService;
    private final OwnerService ownerService;
    private final OwnerRepository ownerRepository;
    private final AdminServiceImpl adminService;
    private final AccountService accountService;
    private final IncomeExpenseItemServiceImpl incomeExpenseItemService;
    private final IncomeExpenseRepository incomeExpenseRepository;
    private final AccountRepository accountRepository;
    private final AccountServiceImpl accountServiceImpl;
    private final CashBoxRepository cashBoxRepository;
    private final CashBoxtValidator cashBoxtValidator;

    private final WebsocketController websocketController;


    @GetMapping
    public String getCashBox(Model model, @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, size = 10) Pageable pageable) {
        Page<CashBox> cashBoxList = cashBoxService.findAll(pageable);
        model.addAttribute("cashBoxList", cashBoxList);
        model.addAttribute("cashBoxSum", cashBoxRepository.sumAmount().orElse(0.0));
        model.addAttribute("accountBalance", accountServiceImpl.getSumOfAccountBalances());
        model.addAttribute("sumDebt", accountServiceImpl.getSumOfAccountDebts());
        model.addAttribute("filterForm", new FilterForm());
        int pageNumber = 0;
        int pageSize = 2;
        Sort sort = Sort.by("fieldName").ascending(); // Сортировка по полю fieldName в порядке возрастания
        Page<Owner>ownerDTOList=ownerService.findAll(PageRequest.of(pageNumber, pageSize, Sort.by("id").ascending()));
        model.addAttribute("owners", ownerDTOList);

        List<IncomeExpenseItems>incomeExpenseItems=incomeExpenseItemService.findAll();
        model.addAttribute("incomeExpenseItems", incomeExpenseItems);


        return "admin_panel/cash_box/cashboxes";
    }

    @GetMapping("/show-incomes")
    public String showAccountIncomePage(@RequestParam Long account_id, Model model) {
        List<CashBox> cashBoxList;
        cashBoxList = accountService.findAccountById(account_id).getTransactions();

        // -- перегоняю List в Page для того , чтобы в html-страничке ничего не ломалось --
        Pageable pageable = PageRequest.of(0, 5);
        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), cashBoxList.size());
        final Page<CashBox> cashBoxPage = new PageImpl<>(cashBoxList.subList(start, end), pageable, cashBoxList.size());
        // -- перегоняю List в Page для того , чтобы в html-страничке ничего не ломалось --

        model.addAttribute("cashBoxList", cashBoxPage);
        model.addAttribute("cashBoxSum", cashBoxRepository.sumAmount().orElse(0.0));
        model.addAttribute("accountBalance", accountServiceImpl.getSumOfAccountBalances());
        model.addAttribute("sumDebt", accountServiceImpl.getSumOfAccountDebts());
        model.addAttribute("filterForm", new FilterForm());
        int pageNumber = 0;
        int pageSize = 2;
        Sort sort = Sort.by("fieldName").ascending(); // Сортировка по полю fieldName в порядке возрастания
        Page<Owner>ownerDTOList=ownerService.findAll(PageRequest.of(pageNumber, pageSize, Sort.by("id").ascending()));
        model.addAttribute("owners", ownerDTOList);

        List<IncomeExpenseItems>incomeExpenseItems=incomeExpenseItemService.findAll();
        model.addAttribute("incomeExpenseItems", incomeExpenseItems);

        return "admin_panel/cash_box/cashboxes";
    }

    @PostMapping("/filter")
    public String filterApartments(Model model, @ModelAttribute FilterForm filterForm, @RequestParam(name = "id",required = false) Long id, @RequestParam(name = "date",required = false) String date,
                                   @RequestParam(name = "isCompleted",required = false) String isCompleted, @RequestParam(name = "incomeExpenseItem",required = false) String incomeExpenseItem,
                                   @RequestParam(name = "owner", required = false) Long owner,  @RequestParam(name = "accountNumber", required = false) Long accountNumber,
                                   @RequestParam(name = "incomeExpenseType",required = false) String incomeExpenseType,
                                   @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, size = 10) Pageable pageable) throws IOException {
        filterForm.setOwnerEntity(ownerService.findByIdDTO(owner));
        String[] dateArray = date.split(" - ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate startDate = LocalDate.parse(dateArray[0], formatter);
        LocalDate endDate = LocalDate.parse(dateArray[1], formatter);

        Page<CashBox> cashBoxList = cashBoxRepository.findByFilters(id,startDate, endDate, cashBoxService.getIsCompleteFromString(isCompleted), incomeExpenseItem,owner,accountNumber, cashBoxService.getIncomeExpenseTypeFromString(incomeExpenseType), pageable);

        model.addAttribute("cashBoxList", cashBoxList);
        model.addAttribute("cashBoxSum", cashBoxRepository.sumAmount());
        model.addAttribute("accountBalance", accountRepository.getSumOfAccountBalances());
        model.addAttribute("sumDebt", accountRepository.getSumOfAccountDebts());
        List<OwnerDTO>ownerDTOList=ownerService.findAllDTO();
        model.addAttribute("owners", ownerDTOList);

        List<IncomeExpenseItems>incomeExpenseItems=incomeExpenseItemService.findAll();
        model.addAttribute("incomeExpenseItems", incomeExpenseItems);
        model.addAttribute("filterForm", filterForm);

        return "admin_panel/cash_box/cashboxes";


        }

    @GetMapping("/{id}")
    public String getCashBox(@PathVariable("id") Long id, Model model) {
        CashBox cashBox = cashBoxService.findById(id);
        model.addAttribute("cashBoxItem", cashBox);
        return "admin_panel/cash_box/cashbox";
    }

    @GetMapping("/newIncome")
    public String createCashBoxIn(@RequestParam(required = false) Long account_id, Model model) {
        List<IncomeExpenseItems>incomeItemsList=incomeExpenseRepository.findAllByIncomeExpenseType(IncomeExpenseType.INCOME);
        model.addAttribute("incomeItemsList", incomeItemsList);

        List<AdminDTO>adminDTOList = adminService.findAllDTO();

        // получение только юзеров с ролями "админ", "директор", "бухгалтер", без сантехников и т.д.
        adminDTOList = adminDTOList.stream()
                .filter(admin -> admin.getRole() == UserRole.ROLE_ADMIN ||
                        admin.getRole() == UserRole.ROLE_DIRECTOR ||
                        admin.getRole() == UserRole.ROLE_MANAGER ||
                        admin.getRole() == UserRole.ROLE_ACCOUNTANT)
                .collect(Collectors.toList());
        model.addAttribute("admins", adminDTOList);

        List<OwnerDTO> ownerDTOList = ownerService.findAllDTO();
        model.addAttribute("owners", ownerDTOList);

        List<ApartmentAccountDTO> apartmentAccountDTOS = accountService.findAllAccounts().stream().map(MappingUtils::fromAccountToDTO).collect(Collectors.toList());
        model.addAttribute("accounts", apartmentAccountDTOS);

        model.addAttribute("nextId", cashBoxService.getMaxId()+1);

        CashBox cashBox = new CashBox();

        if(account_id != null) {
            System.out.println("if block, id is not null");
            ApartmentAccount account = accountService.findAccountById(account_id);
            cashBox.setApartmentAccount(account);
            System.out.println(account.getApartment().getOwner().toString());
        }

        cashBox.setIncomeExpenseType(IncomeExpenseType.INCOME);
        model.addAttribute("cashBoxItem", cashBox);
        return "admin_panel/cash_box/cashbox_edit";
    }

    @GetMapping("/newExpense")
    public String createCashBoxEx(Model model) {
        List<IncomeExpenseItems>expenseItemsList=incomeExpenseRepository.findAllByIncomeExpenseType(IncomeExpenseType.EXPENSE);
        model.addAttribute("expenseItemsList", expenseItemsList);

        model.addAttribute("nextId", cashBoxService.getMaxId()+1);

        List<AdminDTO>adminDTOList = adminService.findAllDTO();
        adminDTOList = adminDTOList.stream()
                .filter(admin -> admin.getRole() == UserRole.ROLE_ADMIN ||
                        admin.getRole() == UserRole.ROLE_DIRECTOR ||
                        admin.getRole() == UserRole.ROLE_MANAGER ||
                        admin.getRole() == UserRole.ROLE_ACCOUNTANT)
                .collect(Collectors.toList());
        model.addAttribute("admins", adminDTOList);

        CashBox cashBox = new CashBox();
        cashBox.setIncomeExpenseType(IncomeExpenseType.EXPENSE);
        model.addAttribute("cashBoxItem", cashBox);
        return "admin_panel/cash_box/cashbox_edit";
    }

    @GetMapping("/edit/{id}")
    public String editeCashBox(@PathVariable("id") Long id, Model model) {
        CashBox cashBox = cashBoxService.findById(id);
        if(cashBox.getIncomeExpenseType().equals(IncomeExpenseType.INCOME)) {

            List<IncomeExpenseItems> incomeItemsList = incomeExpenseRepository.findAllByIncomeExpenseType(IncomeExpenseType.INCOME);
            model.addAttribute("incomeItemsList", incomeItemsList);

            List<OwnerDTO> ownerDTOList = ownerService.findAllDTO();
            model.addAttribute("owners", ownerDTOList);

            List<ApartmentAccountDTO> apartmentAccountDTOS = accountService.findAllAccounts().stream().map(MappingUtils::fromAccountToDTO).collect(Collectors.toList());
            model.addAttribute("accounts", apartmentAccountDTOS);

        } else if (cashBox.getIncomeExpenseType().equals(IncomeExpenseType.EXPENSE)){
            List<IncomeExpenseItems>expenseItemsList=incomeExpenseRepository.findAllByIncomeExpenseType(IncomeExpenseType.EXPENSE);
            model.addAttribute("expenseItemsList", expenseItemsList);
        }

        model.addAttribute("nextId", cashBoxService.getMaxId()+1);

        List<AdminDTO>adminDTOList = adminService.findAllDTO();

        // получение только юзеров с ролями "админ", "директор", "бухгалтер", без сантехников и т.д.
        adminDTOList = adminDTOList.stream()
                .filter(admin -> admin.getRole() == UserRole.ROLE_ADMIN ||
                        admin.getRole() == UserRole.ROLE_DIRECTOR ||
                        admin.getRole() == UserRole.ROLE_MANAGER ||
                        admin.getRole() == UserRole.ROLE_ACCOUNTANT)
                .collect(Collectors.toList());
        model.addAttribute("admins", adminDTOList);

        model.addAttribute("cashBoxItem", cashBox);

        return "admin_panel/cash_box/cashbox_edit";
    }

    @GetMapping("/copy/{id}")
    public String copyCashBox(@PathVariable("id") Long id, Model model) {
        CashBox cashBox = cashBoxService.findById(id);
        if(cashBox.getIncomeExpenseType().equals(IncomeExpenseType.INCOME)) {

            List<IncomeExpenseItems> incomeItemsList = incomeExpenseRepository.findAllByIncomeExpenseType(IncomeExpenseType.INCOME);
            model.addAttribute("incomeItemsList", incomeItemsList);

            List<OwnerDTO> ownerDTOList = ownerService.findAllDTO();
            model.addAttribute("owners", ownerDTOList);

            List<ApartmentAccountDTO> apartmentAccountDTOS = accountService.findAllAccounts().stream().map(MappingUtils::fromAccountToDTO).collect(Collectors.toList());
            model.addAttribute("accounts", apartmentAccountDTOS);

        } else if (cashBox.getIncomeExpenseType().equals(IncomeExpenseType.EXPENSE)){
            List<IncomeExpenseItems>expenseItemsList=incomeExpenseRepository.findAllByIncomeExpenseType(IncomeExpenseType.EXPENSE);
            model.addAttribute("expenseItemsList", expenseItemsList);
        }


        List<AdminDTO>adminDTOList = adminService.findAllDTO();

        // получение только юзеров с ролями "админ", "директор", "бухгалтер", без сантехников и т.д.
        adminDTOList = adminDTOList.stream()
                .filter(admin -> admin.getRole() == UserRole.ROLE_ADMIN ||
                        admin.getRole() == UserRole.ROLE_DIRECTOR ||
                        admin.getRole() == UserRole.ROLE_MANAGER ||
                        admin.getRole() == UserRole.ROLE_ACCOUNTANT)
                .collect(Collectors.toList());
        model.addAttribute("admins", adminDTOList);

        cashBox.setId(null);
        model.addAttribute("cashBoxItem", cashBox);
        return "admin_panel/cash_box/cashbox_edit";
    }

    @PostMapping("/save")
    public String saveCashBox(@Valid @ModelAttribute("cashBoxItem") CashBox cashBoxItem, BindingResult bindingResult, @RequestParam(name = "id", defaultValue = "0") Long id, @RequestParam(name = "owner", defaultValue = "0") Long ownerId,
                              @RequestParam(name = "manager", defaultValue = "0") Long adminId, @RequestParam(name = "apartmentAccount", defaultValue = "0") Long accountId,
                              @RequestParam(name = "amount", defaultValue = "0D") Double amount, @RequestParam(name = "incomeExpenseItems", defaultValue = "0") Long incomeExpenseItemId, Model model) throws IOException {
        cashBoxtValidator.validate(cashBoxItem, bindingResult);

        System.out.println(cashBoxItem);
        System.out.println("bindingResult"+ bindingResult);
        if (bindingResult.hasErrors()) {
            if(cashBoxItem.getIncomeExpenseType().equals(IncomeExpenseType.INCOME)){
                List<IncomeExpenseItems>incomeItemsList=incomeExpenseRepository.findAllByIncomeExpenseType(IncomeExpenseType.INCOME);
                model.addAttribute("incomeItemsList", incomeItemsList);

                List<OwnerDTO> ownerDTOList = ownerService.findAllDTO();
                model.addAttribute("owners", ownerDTOList);

                List<ApartmentAccountDTO> apartmentAccountDTOS = accountService.findAllAccounts().stream().map(MappingUtils::fromAccountToDTO).collect(Collectors.toList());
                model.addAttribute("accounts", apartmentAccountDTOS);
            } else if (cashBoxItem.getIncomeExpenseType().equals(IncomeExpenseType.EXPENSE)){
                List<IncomeExpenseItems>expenseItemsList=incomeExpenseRepository.findAllByIncomeExpenseType(IncomeExpenseType.EXPENSE);
                model.addAttribute("expenseItemsList", expenseItemsList);
            }
            List<AdminDTO>adminDTOList = adminService.findAllDTO();
            model.addAttribute("admins", adminDTOList);

            return "admin_panel/cash_box/cashBoxItem_edit";
        } else {
            if (cashBoxItem.getIncomeExpenseType() == IncomeExpenseType.EXPENSE) {
                if (amount > 0) cashBoxItem.setAmount(amount * (-1));
            } else {
                cashBoxItem.setOwner(ownerService.findById(ownerId));
                ApartmentAccount account = accountService.findAccountById(accountId);
                if(!account.getTransactions().contains(cashBoxItem)) {
                    account.getTransactions().add(cashBoxItem);
                    account.addToBalance(cashBoxItem.getAmount());
                    cashBoxItem.setApartmentAccount(account);
                }
            }
            cashBoxItem.setIncomeExpenseItems(incomeExpenseItemService.findById(incomeExpenseItemId));
            cashBoxItem.setManager(adminService.findAdminById(adminId));
            cashBoxService.save(cashBoxItem);
            return "redirect:/admin/cashbox/";
        }
    }
//

    @PostMapping("/newIncome")
    public String saveNewIncome(@ModelAttribute CashBox cashBoxItem, BindingResult bindingResult, Model model) {
        cashBoxtValidator.validate(cashBoxItem, bindingResult);
        if(bindingResult.hasErrors()) {
            return "admin_panel/cash_box/cashbox_edit";
        }
        cashBoxService.save(cashBoxItem);

        websocketController.sendCashboxItem(cashBoxItem);

        return "redirect:/admin/cashbox";
    }

    @PostMapping("/newExpense")
    public String saveNewExpense(@ModelAttribute CashBox cashBoxItem, BindingResult bindingResult, Model model) {
        cashBoxItem.setAmount(cashBoxItem.getAmount()*-1);
        cashBoxtValidator.validate(cashBoxItem, bindingResult);
        if(bindingResult.hasErrors()) {
            return "admin_panel/cash_box/cashbox_edit";
        }
        cashBoxService.save(cashBoxItem);

        websocketController.sendCashboxItem(cashBoxItem);

        return "redirect:/admin/cashbox";
    }

    @PostMapping("/edit/{id}")
    public String editCashBox(@ModelAttribute CashBox cashBoxItem, BindingResult bindingResult, Model model) {
        cashBoxtValidator.validate(cashBoxItem, bindingResult);
        if(bindingResult.hasErrors()) {
            return "admin_panel/cash_box/cashbox_edit";
        }
        cashBoxService.save(cashBoxItem);

        websocketController.sendCashboxItem(cashBoxItem);

        return "redirect:/admin/cashbox";
    }

    @GetMapping("/delete/{id}")
    public String dellete(@PathVariable("id") Long id) {
        cashBoxService.deleteById(id);
        return "redirect:/admin/cashbox/";
    }

    @GetMapping("/getUsers")
    @ResponseBody
    public Page<OwnerDTO> getOwners(@RequestParam(name = "searchQuerie", defaultValue = "") String searchQuerie,
                                    @RequestParam(name = "page", defaultValue = "0") int page,
                                    @RequestParam(name = "size", defaultValue = "2") int size) {

        System.out.println("page" + page + "size" + size);
        System.out.println("searchQuerie " + searchQuerie);
        Pageable pageable = PageRequest.of(page, size);

        Page<OwnerDTO> ownerPage = ownerService.findByNameFragmentDTO(searchQuerie, pageable);

        return ownerPage;
    }

    @GetMapping("/get-cashbox-page")
    public @ResponseBody Page<CashBoxDTO> getCashbox(@RequestParam Integer page,
                                                     @RequestParam Integer size,
                                                     @RequestParam String filters) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FilterForm form = mapper.readValue(filters, FilterForm.class);
        return cashBoxService.findAllBySpecification2(form, page, size);
    }

    @GetMapping("/get-cashbox-balance")
    public @ResponseBody Double getCashboxBalance() {
        return cashBoxService.calculateBalance();
    }

    @GetMapping("/get-account-balance")
    public @ResponseBody Double getAccountBalance() {
        return accountServiceImpl.getSumOfAccountBalances();
    }

    @GetMapping("/get-account-debts")
    public @ResponseBody Double getAccountDebts() {
        return accountServiceImpl.getSumOfAccountDebts();
    }



}
