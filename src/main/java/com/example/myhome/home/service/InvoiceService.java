package com.example.myhome.home.service;

import com.example.myhome.home.model.*;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.InvoiceComponentRepository;
import com.example.myhome.home.repository.InvoiceRepository;
import com.example.myhome.home.repository.InvoiceTemplateRepository;
import com.example.myhome.home.repository.specifications.InvoiceSpecifications;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
@Log
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceComponentRepository invoiceComponentRepository;

    @Autowired private InvoiceTemplateRepository invoiceTemplateRepository;

    @Autowired
    private ServiceService serviceService;

    @Autowired private ApartmentService apartmentService;
    @Autowired private OwnerService ownerService;

    public List<Invoice> findAllInvoices() {return invoiceRepository.findAll();}

    public List<Invoice> findAllBySpecification(FilterForm filters) {
        log.info("Filters found!");
        log.info(filters.toString());

        Long id = filters.getId();
        InvoiceStatus status = (filters.getStatus() != null) ? InvoiceStatus.valueOf(filters.getStatus()) : null;
        Apartment apartment = (filters.getApartment() != null) ? apartmentService.findByNumber(filters.getApartment()) : null;
        Owner owner = (filters.getOwner() != null) ? ownerService.findById(filters.getOwner()) : null;
        Boolean completed = filters.getCompleted();

        String month = filters.getMonth();
        LocalDate m = (month != null && !month.isEmpty()) ? LocalDate.of(Integer.parseInt(month.split("-")[0]),
                Integer.parseInt(month.split("-")[1]), 1) : null;
        LocalDate m2 = (m != null) ? m.plusMonths(1).minusDays(1) : null;

        String date = filters.getDate();
        LocalDate date_from = null;
        LocalDate date_to = null;
        if(date != null && !date.isEmpty()) {
            date_from = LocalDate.parse(date.split(" to ")[0]);
            date_to = LocalDate.parse(date.split(" to ")[1]);
        }

        Specification<Invoice> specification =
                Specification.where(InvoiceSpecifications.hasId(id)
                        .and(InvoiceSpecifications.hasStatus(status))
                        .and(InvoiceSpecifications.hasApartment(apartment))
                        .and(InvoiceSpecifications.hasOwner(owner))
                        .and(InvoiceSpecifications.isCompleted(completed))
                        .and(InvoiceSpecifications.datesBetween(date_from, date_to))
                        .and(InvoiceSpecifications.datesBetween(m, m2)));

        return invoiceRepository.findAll(specification);
    }

    public List<InvoiceTemplate> findAllTemplates() {return invoiceTemplateRepository.findAll();}

    public Optional<InvoiceTemplate> findDefaultTemplate() {return invoiceTemplateRepository.getDefaultTemplate();}

    public Invoice findInvoiceById(long invoice_id) {return invoiceRepository.findById(invoice_id).orElseThrow();}

    public InvoiceTemplate findTemplateById(long template_id) {
        return invoiceTemplateRepository.findById(template_id).orElseThrow();
    }

    public void setDefaultTemplate(InvoiceTemplate template) {
        Optional<InvoiceTemplate> defaultTemplate = invoiceTemplateRepository.getDefaultTemplate();
        if(defaultTemplate.isPresent()) {
            defaultTemplate.get().setDefault(false);
            invoiceTemplateRepository.save(defaultTemplate.get());
        }
        template.setDefault(true);
        invoiceTemplateRepository.save(template);
    }

    public Long getMaxInvoiceId() {return invoiceRepository.getMaxId().orElse(0L);}

    public Invoice saveInvoice(Invoice invoice) {return invoiceRepository.save(invoice);}

    public InvoiceTemplate saveTemplate(InvoiceTemplate template) {
        return invoiceTemplateRepository.save(template);
    }

    public List<Invoice> saveAllInvoices(List<Invoice> list) {return invoiceRepository.saveAll(list);}
    public List<InvoiceComponents> saveAllInvoicesComponents(List<InvoiceComponents> list) {return invoiceComponentRepository.saveAll(list);}

    public void deleteInvoiceById(long invoice_id) {
        Invoice invoice = findInvoiceById(invoice_id);
        invoiceComponentRepository.deleteAll(invoice.getComponents());
//        invoice.removeAllChildren();
        invoiceRepository.delete(invoice);
    }

    public void deleteTemplateById(long template_id) {
        invoiceTemplateRepository.deleteById(template_id);
    }

    public List<Double> getListSumInvoicesByMonth() {
        List<Double>doubleList = new ArrayList<>();
        LocalDate now = LocalDate.now();
        LocalDate begin = now.minusMonths(11);
        for (int i = 0; i < 12; i++) {
            Double tmp = invoiceRepository.getSumIvoice(begin.getMonthValue(), begin.getYear());
            if (tmp == null) tmp=0D;
            doubleList.add(tmp);
            begin = begin.plusMonths(1);
        }
        return doubleList;
    }

    public List<Double> getListSumPaidInvoicesByMonth() {
        List<Double>doubleList = new ArrayList<>();
        LocalDate now = LocalDate.now();
        LocalDate begin = now.minusMonths(11);
        for (int i = 0; i < 12; i++) {
            Double tmp = invoiceRepository.getSumPaidIvoice(begin.getMonthValue(), begin.getYear(), InvoiceStatus.PAID);
            if (tmp == null) tmp=0D;
//            if (tmp <0) tmp= tmp*-1;
            doubleList.add(tmp);
            begin = begin.plusMonths(1);
        }
        return doubleList;
    }

    public List<String> getListOfMonthName() {
        List<String>doubleList = new ArrayList<>();
        LocalDate now = LocalDate.now();
        LocalDate begin = now.minusMonths(11);
        for (int i = 0; i < 12; i++) {
            String tmp = begin.getMonth().name() + " " + begin.getYear();
            doubleList.add(tmp);
            begin = begin.plusMonths(1);
        }
        return doubleList;
    }

    // ====

    public Invoice buildAndSaveInvoice(Invoice invoice,
                                       String date,
                                       String[] services,
                                       String[] unit_prices,
                                       String[] unit_amounts) {
        invoice.setComponents(new ArrayList<>());
        invoice.setDate(LocalDate.parse(date));
        Invoice savedInvoice = saveInvoice(invoice);

        List<InvoiceComponents> componentsList = buildAndSaveComponents(savedInvoice, services, unit_prices, unit_amounts);
        Double total_price = componentsList.stream().map(InvoiceComponents::getTotalPrice).reduce(Double::sum).orElse(0.0);
        savedInvoice.setTotal_price(total_price);
        invoice.setComponents(componentsList);

        return saveInvoice(savedInvoice);
    }

    public List<InvoiceComponents> buildAndSaveComponents(Invoice invoice,
                                   String[] services,
                                   String[] unit_prices,
                                   String[] unit_amounts) {

        List<InvoiceComponents> componentsList = new ArrayList<>();

        for (int i = 1; i < services.length; i++) {
            InvoiceComponents component = new InvoiceComponents();
            component.setInvoice(invoice);
            component.setService(serviceService.findServiceById(Long.parseLong(services[i])));
            component.setUnit_price(Double.parseDouble(unit_prices[i]));
            component.setUnit_amount(Double.parseDouble(unit_amounts[i]));
            componentsList.add(component);
        }
        Double total_price = componentsList.stream().map(InvoiceComponents::getTotalPrice).reduce(Double::sum).orElse(0.0);
        invoice.setTotal_price(total_price);

        return saveAllInvoicesComponents(componentsList);
    }

}
