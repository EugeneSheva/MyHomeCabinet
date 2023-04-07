package com.example.myhome.home.controller;

import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.model.Invoice;
import com.example.myhome.home.model.InvoiceComponents;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.*;
import com.example.myhome.home.service.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @Autowired private OwnerService ownerService;

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

        model.addAttribute("invoice", invoice);
        model.addAttribute("id", invoiceService.getMaxInvoiceId()+1L);
        model.addAttribute("current_date", LocalDate.now());
        model.addAttribute("buildings", buildingService.findAll());
        model.addAttribute("tariffs", tariffService.findAllTariffs());
        model.addAttribute("meters", (flat_id == null) ? meterDataService.findAllMeters() : meterDataService.findSingleMeterData(flat_id, null));
        model.addAttribute("services", serviceService.findAllServices());
        return "admin_panel/invoices/invoice_card";
    }

    @GetMapping("/update/{id}")
    public String showUpdateInvoicePage(@PathVariable long id, Model model) {

        Invoice invoice = invoiceService.findInvoiceById(id);

        model.addAttribute("flat", invoice.getApartment());

        model.addAttribute("invoice", invoice);
        model.addAttribute("id", invoice.getId());
        model.addAttribute("current_date", LocalDate.now());
        model.addAttribute("buildings", buildingService.findAll());
        model.addAttribute("tariffs", tariffService.findAllTariffs());
        model.addAttribute("meters", meterDataService.findAllMeters());
        model.addAttribute("services", serviceService.findAllServices());
        return "admin_panel/invoices/invoice_card";
    }

    @PostMapping("/create")
    public String createInvoice(@ModelAttribute Invoice invoice,
                                @RequestParam String date,
                                @RequestParam String[] services,
                                @RequestParam String[] unit_prices,
                                @RequestParam String[] unit_amounts) {

        invoiceService.buildAndSaveInvoice(invoice, date, services, unit_prices, unit_amounts);
        return "redirect:/admin/invoices";
    }

    @PostMapping("/update/{id}")
    public String updateInvoice(@PathVariable long id, @ModelAttribute Invoice invoice) {
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
        return "admin_panel/invoices/invoice_print";
    }

    @GetMapping("/template")
    public String getTemplateSettingPage(){
        return "admin_panel/invoices/invoice_template_settings";
    }
}
