package com.example.myhome.home.controller;

import com.example.myhome.home.dto.ApartmentDTO;
import com.example.myhome.home.dto.InvoiceDTO;
import com.example.myhome.home.dto.OwnerDTO;
import com.example.myhome.home.dto.RepairRequestDTO;
import com.example.myhome.home.mapper.OwnerDTOMapper;
import com.example.myhome.home.model.*;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.MessageRepository;
import com.example.myhome.home.repository.RepairRequestRepository;
import com.example.myhome.home.repository.UserRoleRepository;
import com.example.myhome.home.service.*;
import com.example.myhome.home.service.impl.ApartmentServiceImpl;
import com.example.myhome.home.service.impl.InvoiceComponentServiceImpl;
import com.example.myhome.home.validator.OwnerValidator;
import com.example.myhome.home.validator.RequestValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cabinet")
@Log
public class PersonalCabinetController {

    @Autowired
    private OwnerService ownerService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PersistentTokenRepository repository;

    @Autowired
    private PersistentTokenBasedRememberMeServices rememberMeServices;

    @Autowired
    private ApartmentServiceImpl apartmentService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private InvoiceComponentServiceImpl invoiceComponentService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private RepairRequestRepository repairRequestRepository;
    @Autowired
    private RepairRequestService repairRequestService;
    @Autowired
    private OwnerValidator ownerValidator;
    @Autowired
    private OwnerDTOMapper ownerDTOMapper;
    @Autowired
    private RequestValidator validator;
    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String getStartPage(Model model, Principal principal){
        OwnerDTO ownerDTO = ownerService.findOwnerDTObyEmail(principal.getName());
        model.addAttribute("owner", ownerDTO);
        if (ownerDTO.getApartments()!=null && ownerDTO.getApartments().size()>0) {
            model.addAttribute("apartment", apartmentService.findApartmentDto(ownerDTO.getApartments().get(0).getId()));
            model.addAttribute("apartmentBalance", apartmentService.findById(ownerDTO.getApartments().get(0).getId()).getAccount().getBalance());
            model.addAttribute("avgInvoicePriceInMonth", invoiceService.getAverageTotalPriceForApartmentLastYear(ownerDTO.getApartments().get(0).getId()));
            ///1
            Map<String, Double>expenseLastMonth = invoiceComponentService.findExprncesLastMonthByApartment(ownerDTO.getApartments().get(0).getId());
            model.addAttribute("byMonthNames", new ArrayList<>(expenseLastMonth.keySet()));
            model.addAttribute("byMonthValues", new ArrayList<>(expenseLastMonth.values()));
            ///2
            Map<String, Double>expenseThisYear = invoiceComponentService.findExprncesThisYearByApartment(ownerDTO.getApartments().get(0).getId());
            model.addAttribute("byYearName", new ArrayList<>(expenseThisYear.keySet()));
            model.addAttribute("byYearValue", new ArrayList<>(expenseThisYear.values()));
            model.addAttribute("monthsName", invoiceService.getListOfMonthName());
            model.addAttribute("apartExpenseEachMonthByYear", invoiceService.getListExpenseByApartmentByMonth(ownerDTO.getApartments().get(0).getId()));
            model.addAttribute("indexPageActive", true);
            model.addAttribute("apartmentId",ownerDTO.getApartments().get(0).getId());
        }
         return "cabinet/index";
    }

    @GetMapping("/{id}")
    public String getStartPage(Model model, Principal principal, @PathVariable long id){
        OwnerDTO ownerDTO = ownerService.findOwnerDTObyEmail(principal.getName());
        model.addAttribute("owner", ownerDTO);
        model.addAttribute("apartment", apartmentService.findApartmentDto(id));
        model.addAttribute("apartmentBalance", apartmentService.findById(id).getAccount().getBalance());
        model.addAttribute("avgInvoicePriceInMonth", invoiceService.getAverageTotalPriceForApartmentLastYear(id));
        Map<String, Double>expenseLastMonth = invoiceComponentService.findExprncesLastMonthByApartment(id);
        model.addAttribute("byMonthNames", new ArrayList<>(expenseLastMonth.keySet()));
        model.addAttribute("byMonthValues", new ArrayList<>(expenseLastMonth.values()));
        Map<String, Double>expenseThisYear = invoiceComponentService.findExprncesThisYearByApartment(id);
        model.addAttribute("byYearName", new ArrayList<>(expenseThisYear.keySet()));
        model.addAttribute("byYearValue", new ArrayList<>(expenseThisYear.values()));
        model.addAttribute("monthsName", invoiceService.getListOfMonthName());
        model.addAttribute("apartExpenseEachMonthByYear", invoiceService.getListExpenseByApartmentByMonth(id));
        model.addAttribute("indexPageActive", true);
        model.addAttribute("apartmentId",id);


        return "cabinet/index";
    }

    @GetMapping("/invoices")
    public String getInvoicePageByOwner(Model model,FilterForm form, Principal principal) {
        Page<Invoice> invoices;
        if(form.getPage() == null) invoices = invoiceService.findAllBySpecificationAndPage(form, 1, 10);
        else invoices = invoiceService.findAllBySpecificationAndPage(form, form.getPage()-1, 10);
        model.addAttribute("totalPagesCount", invoices.getTotalPages());
        model.addAttribute("filter_form", form);
        OwnerDTO ownerDTO = ownerService.findOwnerDTObyEmail(principal.getName());
        model.addAttribute("owner", ownerDTO);
        model.addAttribute("invoiceList", invoices);
        model.addAttribute("invoicesPageActive", true);
        model.addAttribute("allInvoicesPageActive", true);
        return "cabinet/invoices";
    }

    @GetMapping("/invoices/{id}")
    public String getInvoicePageByApartment(@PathVariable long id, Model model, Principal principal) {
        OwnerDTO ownerDTO = ownerService.findOwnerDTObyEmail(principal.getName());
        model.addAttribute("owner", ownerDTO);
        List<Invoice> invoiceList = invoiceService.findAllByApartmentId(id);
        model.addAttribute("invoiceList", invoiceList);
        model.addAttribute("apart", apartmentService.findApartmentDto(id));
        model.addAttribute("invoicesPageActive", true);
        model.addAttribute("apartmentId",id);
        return "cabinet/invoices";
    }

    @GetMapping("/invoice/{id}")
    public String getInvoiceInfoPageById(@PathVariable long id, Model model, Principal principal) {
        OwnerDTO ownerDTO = ownerService.findOwnerDTObyEmail(principal.getName());
        model.addAttribute("owner", ownerDTO);
        Invoice invoice = invoiceService.findInvoiceById(id);
        model.addAttribute("invoice", invoice);
        model.addAttribute("invoicesPageActive", true);
        model.addAttribute("apartmentId", invoice.getApartment().getId());
        return "cabinet/invoice_card";
    }

    @GetMapping("/tariffs/{id}")
    public String getTariffsPage(@PathVariable long id, Model model, Principal principal) {
        OwnerDTO ownerDTO = ownerService.findOwnerDTObyEmail(principal.getName());
        model.addAttribute("owner", ownerDTO);
        ApartmentDTO apartmentDTO = apartmentService.findApartmentDto(id);
        model.addAttribute("tariff", apartmentService.findById(id).getTariff());
        model.addAttribute("apart", apartmentDTO);
        model.addAttribute("tariffsPageActive", true);
        model.addAttribute("apartmentId",id);
        return "cabinet/tariffs";
    }

    @GetMapping("/messages")
    public String getMessagesPage(Model model, Principal principal,@PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, size = 10) Pageable pageable) {
        OwnerDTO ownerDTO = ownerService.findOwnerDTObyEmail(principal.getName());
        model.addAttribute("owner", ownerDTO);
        List<Message> messagesList = ownerService.findOwnerDTObyEmailWithMessages(principal.getName()).getMessages();
        List<Long> unreadMessagesId = ownerService
                .findByLogin(principal.getName())
                .getUnreadMessages()
                .stream()
                .map(Message::getId)
                .collect(Collectors.toList());
        Page<Message> messagesListPage = new PageImpl<>(messagesList, pageable, messagesList.size());
        model.addAttribute("unreadMessagesId", unreadMessagesId);
        model.addAttribute("messages", messagesListPage);
        model.addAttribute("totalPagesCount", messagesListPage.getTotalPages());
        model.addAttribute("filterForm", new FilterForm());
        model.addAttribute("messagesPageActive", true);
        return "cabinet/messages";
    }

    @GetMapping("/messages/{id}")
    public String getMessageContentPage(@PathVariable long id, Model model, Principal principal) {
        OwnerDTO ownerDTO = ownerService.findOwnerDTObyEmail(principal.getName());
        model.addAttribute("owner", ownerDTO);
        Message message = messageRepository.getReferenceById(id);
        List<Owner>unreadReceivers = message.getUnreadReceivers();
        Owner owner = ownerService.findByLogin(principal.getName());
        if (unreadReceivers.contains(owner)) unreadReceivers.remove(owner);
        messageRepository.save(message);
        model.addAttribute("message", message);
        model.addAttribute("messagesPageActive", true);
        return "cabinet/message_card";
    }

    @GetMapping("/message/deleteSelected")
    @ResponseBody
    public void deleteSelected(@RequestParam(name = "checkboxList")Long[]checkboxList, Principal principal) {
        Owner owner = ownerService.findByLogin(principal.getName());
        List<Message>messages= owner.getMessages();

        for (Long aLong : checkboxList) {
            Message message = messageService.findById(aLong);
            List<Owner>receivers = message.getReceivers();
            for (Owner receiver : receivers) {
                if (receiver.equals(owner)) {
                    receivers.remove(owner);
                    messages.remove(message);
                    break;
                }
            }
            if (message.getUnreadReceivers().contains(owner)) message.getUnreadReceivers().remove(owner);
            message.setReceivers(receivers);
            messageService.save(message);
        }
    }



    @GetMapping("/requests")
    public String getRequestPage(Model model, Principal principal) {
        OwnerDTO ownerDTO = ownerService.findOwnerDTObyEmail(principal.getName());
        model.addAttribute("owner", ownerDTO);
        FilterForm filterForm = new FilterForm();
        model.addAttribute("filterForm", filterForm);
        model.addAttribute("requestsPageActive", true);
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
        repairRequest.setDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        repairRequest.setTime(LocalTime.now().plusHours(3).format(DateTimeFormatter.ofPattern("HH:mm")));
        model.addAttribute("repairRequest", repairRequest);
        model.addAttribute("masters", userRoleRepository.findAllMasterRoles());
        model.addAttribute("requestsPageActive", true);
        return "cabinet/request_card";
    }
//
    @PostMapping("/request/save")
    public String saveRequest(@ModelAttribute RepairRequest request,
                              BindingResult bindingResult, @RequestParam(name = "id", defaultValue = "0") Long id, @RequestParam("master") Long master,
                              @RequestParam(name = "apartment", defaultValue = "0") Long apartmentId,@RequestParam("description") String description,
                              @RequestParam("date") String date,  @RequestParam("time") String time, Principal principal, Model model) throws IOException {

        Owner owner = ownerService.findByLogin(principal.getName());
        RepairRequest repairRequest = new RepairRequest();
        repairRequest.setId(id);
        if (apartmentId>0) repairRequest.setApartment(apartmentService.findById(apartmentId));
        if (master > 0) repairRequest.setMaster_type(userRoleRepository.findById(master).orElseThrow());
        repairRequest.setDescription(description);
        repairRequest.setDate(date);
        repairRequest.setTime(time);
        LocalDateTime dateTime = LocalDateTime.parse(date + " " + time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        repairRequest.setBest_time_request(dateTime);
        repairRequest.setOwner(owner);
        repairRequest.setStatus(RepairStatus.ACCEPTED);
        repairRequest.setRequest_date(LocalDateTime.now());
        repairRequest.setPhone_number(owner.getPhone_number());

        validator.validate(repairRequest, bindingResult);
        if(bindingResult.hasErrors()) {
            OwnerDTO ownerDTO = ownerService.findOwnerDTObyEmail(principal.getName());
            model.addAttribute("owner", ownerDTO);
            return "cabinet/request_card";
        }
        repairRequestRepository.save(repairRequest);

        return "redirect:/cabinet/requests";
    }

    @GetMapping("/get-requests")
    public @ResponseBody Page<RepairRequestDTO> getRequests(@RequestParam Integer page,
                                                            @RequestParam Integer size,
                                                            @RequestParam String filters, Principal principal) throws JsonProcessingException, IllegalAccessException {
        OwnerDTO ownerDTO = ownerDTOMapper.toDTOcabinetProfile(ownerService.findByLogin(principal.getName()));
        Pageable pageable = PageRequest.of(page-1, size);
        return repairRequestService.findReqoestDtoByOwnerId(ownerDTO.getId(), pageable);
    }


    @GetMapping("/user/view")
    public String getUserProfilePage(Model model, Principal principal) {
        OwnerDTO ownerDTO = ownerDTOMapper.toDTOcabinetProfile(ownerService.findByLogin(principal.getName()));
        model.addAttribute("owner", ownerDTO);
        model.addAttribute("profilePageActive", true);
        return "cabinet/user_profile";
    }

    @GetMapping("/user/edit")
    public String getEditUserProfilePage(Model model, Principal principal) {
        OwnerDTO ownerDTO = ownerDTOMapper.toDTOcabinetEditProfile(ownerService.findByLogin(principal.getName()));
        System.out.println("ownerDTO " + ownerDTO);
        model.addAttribute("owner", ownerDTO);
        model.addAttribute("profilePageActive", true);
        return "cabinet/user_edit";
    }

    @PostMapping("/user/save")
    public String saveCoffee(@Valid @ModelAttribute("owner") OwnerDTO owner, BindingResult bindingResult, @RequestParam("img1") MultipartFile file, @RequestParam("newPassword") String newPassword, @RequestParam("repassword") String repassword, Principal principal) throws IOException {
        Locale locale = LocaleContextHolder.getLocale();
        Owner newOwner = ownerDTOMapper.toEntityСabinetEditProfile(owner);
        ownerValidator.validate(newOwner, bindingResult);
        if(file != null && !file.isEmpty()) {
            if (!file.getContentType().equals("image/jpg") && !file.getContentType().equals("image/jpeg") && !file.getContentType().equals("image/png")) {
                FieldError fileError = new FieldError("owner", "profile_picture", messageSource.getMessage("imgValid", null, locale));
                bindingResult.addError(fileError);
            }
        }
        if (bindingResult.hasErrors()) {
            System.out.println("bindingResult " + bindingResult);
            return "cabinet/user_edit";
        } else if (!newPassword.equals(repassword) ){
            return "cabinet/user_edit";
        } else {
            Owner oldOwner = ownerService.findById(owner.getId());
            newOwner.setProfile_picture(ownerService.saveOwnerImage(owner.getId(), file));
            newOwner.setEnabled(oldOwner.getEnabled());
            if (newPassword != null && newPassword.length() > 0 ) {
                newOwner.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            }else {
                newOwner.setPassword(oldOwner.getPassword());
            }
            ownerService.save(newOwner);
            if (!principal.getName().equals(newOwner.getEmail())) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(newOwner.getEmail(), authentication.getCredentials(), authentication.getAuthorities());
                newAuthentication.setDetails(authentication.getDetails());
                SecurityContextHolder.getContext().setAuthentication(newAuthentication);
            }
        }
        return "redirect:/cabinet/user/view";
    }
    @GetMapping(value="/get-invoices-cabinet")
    public @ResponseBody Page<InvoiceDTO> getInvoices(@RequestParam Integer page, @RequestParam Integer size,
                                                      @RequestParam String filters, Principal principal) throws JsonProcessingException {
        System.out.println("AJAX page size filter "+ page + " " + size + " " + filters);
        Owner owner = ownerService.findByLogin(principal.getName());
        ObjectMapper mapper = new ObjectMapper();
        FilterForm form = mapper.readValue(filters, FilterForm.class);
        return invoiceService.findAllBySpecificationAndPageCabinet(form, page, size, owner);
    }

    @GetMapping("/get-messages")
    public @ResponseBody Page<Message> getOwners(@RequestParam Integer page,
                                                  @RequestParam Integer size,
                                                  @RequestParam String filters, Principal principal) throws JsonProcessingException {
        Owner owner = ownerService.findByLogin(principal.getName());
        ObjectMapper mapper = new ObjectMapper();
        FilterForm form = mapper.readValue(filters, FilterForm.class);
        return messageService.findAllBySpecification(form, page, size, owner.getId());
    }
}
