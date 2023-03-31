package com.example.myhome.home.controller;

import com.example.myhome.home.model.Invoice;
import com.example.myhome.home.model.InvoiceComponents;
import com.example.myhome.home.repos.*;
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
    private InvoiceRepository invoiceRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private TariffRepository tariffRepository;

    @Autowired
    private MeterDataRepository meterDataRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private InvoiceComponentRepository invoiceComponentRepository;

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

    @PostMapping("/create")
    public String createInvoice(@ModelAttribute Invoice invoice,
                                @RequestParam String date,
                                @RequestParam String[] services,
                                @RequestParam String[] unit_prices,
                                @RequestParam String[] unit_amounts) {

        invoice.setComponents(new ArrayList<>());
        log.info(Arrays.toString(services));
        log.info(Arrays.toString(unit_prices));
        log.info(Arrays.toString(unit_amounts));

        invoice.setDate(LocalDate.parse(date));
        Invoice savedInvoice = invoiceRepository.save(invoice);

        List<InvoiceComponents> componentsList = new ArrayList<>();

        for (int i = 1; i < services.length; i++) {
            InvoiceComponents component = new InvoiceComponents();
            component.setInvoice(savedInvoice);
            component.setService(serviceRepository.findById(Long.parseLong(services[i])).orElseThrow());
            component.setUnit_price(Double.parseDouble(unit_prices[i]));
            component.setUnit_amount(Double.parseDouble(unit_amounts[i]));
            componentsList.add(component);
        }
        Double total_price = componentsList.stream().map(InvoiceComponents::getTotalPrice).reduce(Double::sum).orElse(0.0);
        savedInvoice.setTotal_price(total_price);

        invoiceComponentRepository.saveAll(componentsList);
        invoiceRepository.save(savedInvoice);

        return "redirect:/admin/invoices";
    }

    @PostMapping("/update/{id}")
    public String updateInvoice(@PathVariable long id, @ModelAttribute Invoice invoice) {
        invoiceRepository.save(invoice);
        return "redirect:/admin/invoices";
    }

    @GetMapping("/delete/{id}")
    public String deleteInvoice(@PathVariable long id) {
        invoiceRepository.deleteById(id);
        return "redirect:/admin/invoices";
    }

    @GetMapping("/delete-invoice")
    public @ResponseBody String deleteInvoiceFromTable(@RequestParam long id) {
        invoiceRepository.deleteById(id);
        return "Удалил квитанцию с ID " + id;
    }
}
