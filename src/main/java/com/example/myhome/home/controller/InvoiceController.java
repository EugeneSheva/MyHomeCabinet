package com.example.myhome.home.controller;

import com.example.myhome.home.model.Invoice;
import com.example.myhome.home.model.InvoiceComponents;
import com.example.myhome.home.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private TariffRepository tariffRepository;

    @Autowired
    private MeterDataRepository meterDataRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping
    public String showInvoicePage(Model model) {
        model.addAttribute("invoices", invoiceRepository.findAll());
        return "invoices";
    }

    @GetMapping("/{id}")
    public String showInvoiceInfo(@PathVariable long id, Model model) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow();
        Double total_price = invoice.getComponents()
                .stream()
                .map(InvoiceComponents::getTotalPrice)
                .reduce(Double::sum)
                .orElse(0.0);
        model.addAttribute("invoice", invoice);
        model.addAttribute("total_price", total_price);
        return "invoice_profile";
    }

    @GetMapping("/create")
    public String showCreateInvoicePage(Model model) {
        model.addAttribute("invoice", new Invoice());
        model.addAttribute("id", invoiceRepository.getMaxId().orElse(0L)+1);
        model.addAttribute("current_date", LocalDate.now());
        model.addAttribute("buildings", buildingRepository.findAll());
        model.addAttribute("tariffs", tariffRepository.findAll());
        model.addAttribute("meters", meterDataRepository.findAll());
        model.addAttribute("services", serviceRepository.findAll());
        return "invoice_card";
    }

    @GetMapping("/update/{id}")
    public String showUpdateInvoicePage(@PathVariable long id, Model model) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow();
        model.addAttribute("invoice", invoice);
        model.addAttribute("id", invoice.getId());
        model.addAttribute("current_date", LocalDate.now());
        model.addAttribute("buildings", buildingRepository.findAll());
        model.addAttribute("tariffs", tariffRepository.findAll());
        model.addAttribute("meters", meterDataRepository.findAll());
        model.addAttribute("services", serviceRepository.findAll());
        return "invoice_card";
    }
}
