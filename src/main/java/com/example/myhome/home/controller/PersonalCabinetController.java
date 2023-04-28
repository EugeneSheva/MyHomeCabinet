package com.example.myhome.home.controller;

import com.example.myhome.home.configuration.security.CustomUserDetails;
import com.example.myhome.home.model.*;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.*;
import com.example.myhome.home.service.*;
import com.example.myhome.home.validator.OwnerValidator;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cabinet")
@Log
public class PersonalCabinetController {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private PersistentTokenRepository repository;

    @Autowired
    private PersistentTokenBasedRememberMeServices rememberMeServices;

    @Autowired
    private ApartmentService apartmentService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private InvoiceComponentService invoiceComponentService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private RepairRequestRepository repairRequestRepository;
    @Autowired
    private OwnerValidator ownerValidator;

    @GetMapping
    public String getStartPage(Model model, Principal principal){
        OwnerDTO ownerDTO = ownerService.findOwnerDTObyEmail(principal.getName());
        model.addAttribute("owner", ownerDTO);
        model.addAttribute("apartment", apartmentService.findApartmentDto(ownerDTO.getApartments().get(0).getId()));
        model.addAttribute("avgInvoicePriceInMonth", invoiceService.getAverageTotalPriceForApartmentLastYear(ownerDTO.getApartments().get(0).getId()));

        Map<String, Double>expenseLastMonth = invoiceComponentService.findExprncesLastMonthByApartment(ownerDTO.getApartments().get(0).getId());
        System.out.println("expenseLastMonth"+ expenseLastMonth);
        model.addAttribute("byMonthNames", new ArrayList<>(expenseLastMonth.keySet()));
        model.addAttribute("byMonthValues", new ArrayList<>(expenseLastMonth.values()));

        Map<String, Double>expenseThisYear = invoiceComponentService.findExprncesThisYearByApartment(ownerDTO.getApartments().get(0).getId());
        System.out.println("expenseThisYear"+ expenseThisYear);
        model.addAttribute("byYearName", new ArrayList<>(expenseThisYear.keySet()));
        model.addAttribute("byYearValue", new ArrayList<>(expenseThisYear.values()));

        model.addAttribute("monthsName", invoiceService.getListOfMonthName());
        model.addAttribute("apartExpenseEachMonthByYear", invoiceService.getListExpenseByApartmentByMonth(ownerDTO.getApartments().get(0).getId()));
        return "cabinet/index";
    }

    @GetMapping("/{id}")
    public String getStartPage(Model model, Principal principal, @PathVariable long id){
        OwnerDTO ownerDTO = ownerService.findOwnerDTObyEmail(principal.getName());
        model.addAttribute("owner", ownerDTO);
        model.addAttribute("apartment", apartmentService.findApartmentDto(id));
        model.addAttribute("avgInvoicePriceInMonth", invoiceService.getAverageTotalPriceForApartmentLastYear(id));

        Map<String, Double>expenseLastMonth = invoiceComponentService.findExprncesLastMonthByApartment(id);
        System.out.println("expenseLastMonth"+ expenseLastMonth);
        model.addAttribute("byMonthNames", new ArrayList<>(expenseLastMonth.keySet()));
        model.addAttribute("byMonthValues", new ArrayList<>(expenseLastMonth.values()));

        Map<String, Double>expenseThisYear = invoiceComponentService.findExprncesThisYearByApartment(id);
        System.out.println("expenseThisYear"+ expenseThisYear);
        model.addAttribute("byYearName", new ArrayList<>(expenseThisYear.keySet()));
        model.addAttribute("byYearValue", new ArrayList<>(expenseThisYear.values()));

        model.addAttribute("monthsName", invoiceService.getListOfMonthName());
        model.addAttribute("apartExpenseEachMonthByYear", invoiceService.getListExpenseByApartmentByMonth(id));
        return "cabinet/index";
    }

    @GetMapping("/invoices")
    public String getInvoicePageByOwner(Model model, Principal principal) {
        OwnerDTO ownerDTO = ownerService.findOwnerDTObyEmail(principal.getName());
        model.addAttribute("owner", ownerDTO);

        List<Invoice> invoiceList = invoiceService.findAllByOwnerId(ownerDTO.getId());
        model.addAttribute("invoiceList", invoiceList);
        return "cabinet/invoices";
    }

    @GetMapping("/invoices/{id}")
    public String getInvoicePageByApartment(@PathVariable long id, Model model, Principal principal) {
        OwnerDTO ownerDTO = ownerService.findOwnerDTObyEmail(principal.getName());
        model.addAttribute("owner", ownerDTO);

        List<Invoice> invoiceList = invoiceService.findAllByApartmentId(id);
        model.addAttribute("invoiceList", invoiceList);
        model.addAttribute("apart", apartmentService.findApartmentDto(id));
        return "cabinet/invoices";
    }

    @GetMapping("/invoice/{id}")
    public String getInvoiceInfoPageById(@PathVariable long id, Model model, Principal principal) {
        OwnerDTO ownerDTO = ownerService.findOwnerDTObyEmail(principal.getName());
        model.addAttribute("owner", ownerDTO);
        Invoice invoice = invoiceService.findInvoiceById(id);
        model.addAttribute("invoice", invoice);
        return "cabinet/invoice_card";
    }

    @GetMapping("/tariffs/{id}")
    public String getTariffsPage(@PathVariable long id, Model model, Principal principal) {
        OwnerDTO ownerDTO = ownerService.findOwnerDTObyEmail(principal.getName());
        model.addAttribute("owner", ownerDTO);
        ApartmentDTO apartmentDTO = apartmentService.findApartmentDto(id);
        model.addAttribute("tariff", apartmentService.findById(id).getTariff());
        model.addAttribute("apart", apartmentDTO);
        return "cabinet/tariffs";
    }

    @GetMapping("/messages")
    public String getMessagesPage(Model model, Principal principal) {
        OwnerDTO ownerDTO = ownerService.findOwnerDTObyEmail(principal.getName());
        model.addAttribute("owner", ownerDTO);

        List<Message> messagesList = ownerService.findOwnerDTObyEmailWithMessages(principal.getName()).getMessages();
        model.addAttribute("messages", messagesList);
        return "cabinet/messages";
    }

    @GetMapping("/messages/{id}")
    public String getMessageContentPage(@PathVariable long id, Model model, Principal principal) {
        OwnerDTO ownerDTO = ownerService.findOwnerDTObyEmail(principal.getName());
        model.addAttribute("owner", ownerDTO);
        return "cabinet/message_card";
    }

    @GetMapping("/message/deleteSelected")
    @ResponseBody
    public void deleteSelected(@RequestParam(name = "checkboxList")Long[]checkboxList, Principal principal) {
        List<Message>messages= ownerService.findByLogin(principal.getName()).getMessages();
        for (Long aLong : checkboxList) {
            Message message = messageService.findById(aLong);
            List<Owner>receivers = message.getReceivers();
            for (Owner owner : receivers) {
                if (owner.getEmail().equals(principal.getName())) {
                    receivers.remove(owner);
                    messages.remove(message);
                    break;
                }
            }
            message.setReceivers(receivers);
            messageService.save(message);
        }
    }



    @GetMapping("/requests")
    public String getRequestPage(Model model, Principal principal) {
        OwnerDTO ownerDTO = ownerService.findOwnerDTObyEmail(principal.getName());
        model.addAttribute("owner", ownerDTO);
        model.addAttribute("repairRequests", repairRequestRepository.findAllByOwnerId(ownerDTO.getId()));
        FilterForm filterForm = new FilterForm();
        model.addAttribute("filterForm", filterForm);
        return "cabinet/requests";
    }

    @GetMapping("/request/delete/{id}")
    public String deleteRequest(@PathVariable long id) {
        repairRequestRepository.deleteById(id);
        return "redirect:/cabinet/requests";
    }

    @GetMapping("/requests/create")
    public String getCreateRequestPage(Model model, Principal principal) {
        OwnerDTO ownerDTO = ownerService.findOwnerDTObyEmail(principal.getName());
        model.addAttribute("owner", ownerDTO);
        RepairRequest repairRequest = new RepairRequest();
        repairRequest.setDate(LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        repairRequest.setTime(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        System.out.println(repairRequest);
        model.addAttribute("repairRequest", repairRequest);
        return "cabinet/request_card";
    }

    @PostMapping("/request/save")
    public String saveRequest(@RequestParam(name = "id", defaultValue = "0") Long id, @RequestParam("master") String master,
                              @RequestParam(name = "apartment") Long apartmentId,@RequestParam("description") String description,
                              @RequestParam("date") String date,  @RequestParam("time") String time, Principal principal) throws IOException {
        System.out.println(id +' '+ master +' '+ apartmentId+' '+ description+' '+ date+' '+ time);
        Owner owner = ownerService.findByLogin(principal.getName());
        RepairRequest repairRequest = new RepairRequest();
        repairRequest.setId(id);
        repairRequest.setApartment(apartmentService.findById(apartmentId));
        repairRequest.setMaster_type(RepairMasterType.valueOf(master));
        repairRequest.setDescription(description);
        repairRequest.setDate(date);
        repairRequest.setTime(time);
        LocalDateTime dateTime = LocalDateTime.parse(date + " " + time, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
        repairRequest.setBest_time_request(dateTime);
        repairRequest.setOwner(owner);
        repairRequest.setStatus(RepairStatus.ACCEPTED);
        repairRequest.setRequest_date(LocalDateTime.now());
        repairRequest.setPhone_number(owner.getPhone_number());
        repairRequestRepository.save(repairRequest);

        return "redirect:/cabinet/requests";

    }


    @GetMapping("/user/view")
    public String getUserProfilePage(Model model, Principal principal) {
        OwnerDTO ownerDTO = ownerService.findOwnerDTObyEmailFull(principal.getName());
        model.addAttribute("owner", ownerDTO);
        return "cabinet/user_profile";
    }

    @GetMapping("/user/edit")
    public String getEditUserProfilePage(Model model, Principal principal) {
        model.addAttribute("owner", ownerService.findByLogin(principal.getName()));
        return "cabinet/user_edit";
    }

    @PostMapping("/user/save")
    public String saveCoffee(@Valid @ModelAttribute("owner") Owner owner, BindingResult bindingResult, @RequestParam("img1") MultipartFile file) throws IOException {
        ownerValidator.validate(owner, bindingResult);
        if (bindingResult.hasErrors()) {
            return "cabinet/user_edit";
        } else {
            owner.setProfile_picture(ownerService.saveOwnerImage(owner.getId(), file));
            ownerService.save(owner);
        }
        return "redirect:/cabinet/user/view";
    }
}
