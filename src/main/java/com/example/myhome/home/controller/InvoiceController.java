package com.example.myhome.home.controller;

import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.model.Invoice;
import com.example.myhome.home.model.InvoiceComponents;
import com.example.myhome.home.model.InvoiceTemplate;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.*;
import com.example.myhome.home.service.*;
import com.example.myhome.home.validator.InvoiceValidator;
import com.example.myhome.util.FileUploadUtil;
import lombok.extern.java.Log;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/invoices")
@Log
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private CashBoxService cashBoxService;

    @Autowired
    private ApartmentAccountService apartmentAccountService;

    @Autowired
    private ApartmentService apartmentService;

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private TariffService tariffService;

    @Autowired
    private MeterDataService meterDataService;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    @Autowired private OwnerService ownerService;

    @Autowired
    private InvoiceValidator validator;

    @GetMapping
    public String showInvoicePage(Model model,
                                  FilterForm form) throws IllegalAccessException {

        List<Invoice> invoices;

        if(!form.filtersPresent()) invoices = invoiceService.findAllInvoices();
        else invoices = invoiceService.findAllBySpecification(form);

        model.addAttribute("invoices", invoices);
        model.addAttribute("owners", ownerService.findAllDTO());
        model.addAttribute("cashbox_balance", cashBoxService.calculateBalance());
        model.addAttribute("account_balance", apartmentAccountService.getSumOfAccountBalances());
        model.addAttribute("account_debt", apartmentAccountService.getSumOfAccountDebts());

        model.addAttribute("filter_form", form);
        return "admin_panel/invoices/invoices";
    }

    @GetMapping("/search")
    public String showSearch(@RequestParam long flat_id, Model model) {
        Apartment apartment = apartmentService.findById(flat_id);
        model.addAttribute("invoices", apartment.getInvoiceList());
        return "admin_panel/invoices/invoices";
    }

    @GetMapping("/{id}")
    public String showInvoiceInfo(@PathVariable long id, Model model) {
        Invoice invoice = invoiceService.findInvoiceById(id);
        Double total_price = invoice.getComponents()
                .stream()
                .map(InvoiceComponents::getTotalPrice)
                .reduce(Double::sum)
                .orElse(0.0);
        model.addAttribute("invoice", invoice);
        model.addAttribute("total_price", total_price);
        return "admin_panel/invoices/invoice_profile";
    }

    @GetMapping("/create")
    public String showCreateInvoicePage(@RequestParam(required = false) Long flat_id, Model model) {
        Invoice invoice = new Invoice();
        if(flat_id != null) invoice.setApartment(apartmentService.findById(flat_id));
        model.addAttribute("id", invoiceService.getMaxInvoiceId()+1L);

        model.addAttribute("invoice", invoice);
        model.addAttribute("meters", (flat_id == null) ? meterDataService.findAllMeters() : meterDataService.findSingleMeterData(flat_id, null));
        return "admin_panel/invoices/invoice_card";
    }

    @GetMapping("/update/{id}")
    public String showUpdateInvoicePage(@PathVariable long id, Model model) {

        Invoice invoice = invoiceService.findInvoiceById(id);

        model.addAttribute("flat", invoice.getApartment());
        model.addAttribute("id", id);
        model.addAttribute("invoice", invoice);
        model.addAttribute("meters", meterDataService.findSingleMeterData(id, null));
        return "admin_panel/invoices/invoice_card";
    }

    @PostMapping("/create")
    public String createInvoice(@ModelAttribute Invoice invoice,
                                BindingResult bindingResult,
                                @RequestParam String date,
                                @RequestParam String[] services,
                                @RequestParam String[] unit_prices,
                                @RequestParam String[] unit_amounts) {
        log.info(invoice.toString());

        validator.validate(invoice, bindingResult);
        if(bindingResult.hasErrors()) {
            log.info("Errors found");
            log.info(bindingResult.getAllErrors().toString());
            return "admin_panel/invoices/invoice_card";
        }
        invoiceService.buildAndSaveInvoice(invoice, date, services, unit_prices, unit_amounts);
        return "redirect:/admin/invoices";
    }

    @PostMapping("/update/{id}")
    public String updateInvoice(@PathVariable long id, @ModelAttribute Invoice invoice, BindingResult bindingResult) {
        log.info(invoice.toString());

        validator.validate(invoice, bindingResult);
        if(bindingResult.hasErrors()) {
            log.info("Errors found");
            log.info(bindingResult.getAllErrors().toString());
            return "admin_panel/invoices/invoice_card";
        }
        invoice.getComponents().forEach(comp -> comp.setInvoice(invoice));
        invoiceService.saveInvoice(invoice);
        invoiceService.saveAllInvoicesComponents(invoice.getComponents());
        return "redirect:/admin/invoices";
    }

    @GetMapping("/delete/{id}")
    public String deleteInvoice(@PathVariable long id) {
        invoiceService.deleteInvoiceById(id);
        return "redirect:/admin/invoices";
    }

    @GetMapping("/delete-invoice")
    public @ResponseBody String deleteInvoiceFromTable(@RequestParam long id) {
        invoiceService.deleteInvoiceById(id);
        return "Удалил квитанцию с ID " + id;
    }

    @GetMapping("/print/{id}")
    public String getPrintPage(@PathVariable long id, Model model) {
        Invoice invoice = invoiceService.findInvoiceById(id);
        model.addAttribute("invoice", invoice);
        model.addAttribute("default_template", invoiceService.findDefaultTemplate().orElse(null));
        model.addAttribute("templates", invoiceService.findAllTemplates());
        return "admin_panel/invoices/invoice_print";
    }

    @GetMapping("/template")
    public String getTemplateSettingPage(Model model,
                                         @RequestParam(required = false) Long default_id,
                                         @RequestParam(required = false) Long delete_id){

        if(default_id != null) {
            invoiceService.setDefaultTemplate(invoiceService.findTemplateById(default_id));
            return "redirect:/admin/invoices/template";
        } else if(delete_id != null) {
            invoiceService.deleteTemplateById(delete_id);
            return "redirect:/admin/invoices/template";
        }
        model.addAttribute("default_template", invoiceService.findDefaultTemplate().orElse(null));
        model.addAttribute("templates", invoiceService.findAllTemplates());
        return "admin_panel/invoices/invoice_template_settings";
    }

    @PostMapping("/template")
    public String saveTemplate(@RequestParam String name,
                               @RequestParam MultipartFile file) throws IOException {
        InvoiceTemplate template = new InvoiceTemplate();
        template.setName(name);
        if(file.getSize() > 0) {
            String file_extension = FilenameUtils.getExtension(file.getOriginalFilename());
            log.info(file_extension);
            assert file_extension != null;
            if(file_extension.equalsIgnoreCase("xls") || file_extension.equalsIgnoreCase("xlsx")) {
                fileUploadUtil.saveFile("/files/", file.getOriginalFilename(), file);
                template.setFile(file.getOriginalFilename());
            }
        }
        invoiceService.saveTemplate(template);
        return "redirect:/admin/invoices/template";
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("current_date", LocalDate.now());
        model.addAttribute("buildings", buildingService.findAll());
        model.addAttribute("tariffs", tariffService.findAllTariffs());
        model.addAttribute("services", serviceService.findAllServices());
    }
}
