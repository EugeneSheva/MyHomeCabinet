package com.example.myhome.home.service;

import com.example.myhome.home.model.*;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.InvoiceComponentRepository;
import com.example.myhome.home.repository.InvoiceRepository;
import com.example.myhome.home.repository.InvoiceTemplateRepository;
import com.example.myhome.home.repository.PaymentDetailsRepository;
import com.example.myhome.home.repository.specifications.InvoiceSpecifications;
import com.example.myhome.util.ExcelHelper;
import lombok.extern.java.Log;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    @Autowired private PaymentDetailsRepository paymentDetailsRepository;

    @Autowired private ApartmentService apartmentService;
    @Autowired private OwnerService ownerService;
    @Autowired private EmailService emailService;

    @Autowired private ExcelHelper excelHelper;

    private static final String FILE_PATH = "C:\\Users\\OneSmiLe\\IdeaProjects\\MyHome\\src\\main\\resources\\static\\files\\";

    public List<Invoice> findAllInvoices() {return invoiceRepository.findAll();}

    public List<Invoice> findAllByPage(Integer page, Integer page_size) {
        return invoiceRepository.findAll(PageRequest.of(page, page_size)).toList();
    }

    public List<Invoice> findAllBySpecification(FilterForm filters) {
        log.info("Filters found!");
        log.info(filters.toString());

        Specification<Invoice> specification = buildSpecFromFilters(filters);

        return invoiceRepository.findAll(specification);
    }

    public Page<Invoice> findAllBySpecificationAndPage(FilterForm filters, Integer page, Integer page_size) {
        log.info("Filters found!");
        log.info(filters.toString());

        Specification<Invoice> specification = buildSpecFromFilters(filters);

        Pageable pageable = PageRequest.of(page, page_size);

        return invoiceRepository.findAll(specification, pageable);
    }

    public Long count() {return invoiceRepository.count();}

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

    public List<Invoice>findAllByApartmentId(Long id) { return invoiceRepository.findAllByApartmentId(id);}
    public List<Invoice>findAllByOwnerId(Long id){ return invoiceRepository.findAllByOwnerId(id);}


    public void deleteInvoiceById(long invoice_id) {
        Invoice invoice = findInvoiceById(invoice_id);
        invoice.getComponents().clear();
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

    public List<Double> getListExpenseByApartmentByMonth(Long id) {
        List<Double>doubleList = new ArrayList<>();
        LocalDate now = LocalDate.now();
        LocalDate begin = now.minusMonths(11);
        for (int i = 0; i < 12; i++) {
            Double tmp = invoiceRepository.getTotalPriceByApartmentIdAndMonthAndYear(id, begin.getMonthValue(), begin.getYear());
            if (tmp == null) tmp=0D;
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

    public Invoice buildInvoice(Invoice invoice,
                               String date,
                               String[] services,
                               String[] unit_prices,
                               String[] unit_amounts) {
        log.info("Building invoice!");
        invoice.setDate(LocalDate.parse(date));
        log.info("Set date " + LocalDate.parse(date));
        log.info("Adding components");
        invoice = buildComponents(invoice, services, unit_prices, unit_amounts);
        log.info("Built invoice!");
        log.info(invoice.toString());

//        savedInvoice = buildComponents(savedInvoice, services, unit_prices, unit_amounts);

        return invoice;
    }

    public Invoice buildComponents(Invoice invoice,
                                   String[] services,
                                   String[] unit_prices,
                                   String[] unit_amounts) {

        log.info("Clearing old components list, if present");
        invoice.getComponents().clear();
        log.info("Old components cleared");

        for (int i = 1; i < services.length; i++) {

            //checking component integrity
            if(services[i].isEmpty() || unit_amounts[i].isEmpty() || unit_prices[i].isEmpty()) continue;
            try {
                Double.parseDouble(unit_prices[i]);
                Double.parseDouble(unit_amounts[i]);
            } catch (NumberFormatException e) {
                log.info("Incorrect component found, skipping");
                continue;
            }

            InvoiceComponents component = new InvoiceComponents();
            component.setInvoice(invoice);
            component.setService(serviceService.findServiceById(Long.parseLong(services[i])));
            component.setUnit_price(Double.parseDouble(unit_prices[i]));
            component.setUnit_amount(Double.parseDouble(unit_amounts[i]));
            invoice.getComponents().add(component);
        }
        log.info("New components set!");
        Double total_price = invoice.getComponents().stream().map(InvoiceComponents::getTotalPrice).reduce(Double::sum).orElse(0.0);
        invoice.setTotal_price(total_price);
        log.info("Total price of an invoice: " + total_price);
        return invoice;
    }

    public Specification<Invoice> buildSpecFromFilters(FilterForm filters) {
        Long id = filters.getId();
        InvoiceStatus status = (filters.getStatus() != null) ? InvoiceStatus.valueOf(filters.getStatus()) : null;
//        Apartment apartment = (filters.getApartment() != null) ? apartmentService.findByNumber(filters.getApartment()) : null;
        Long apartment = filters.getApartment() != null ? filters.getApartment() : null;
        Owner owner = (filters.getOwner() != null) ? ownerService.findById(filters.getOwner()) : null;
        Boolean completed = filters.getCompleted();

        String month = filters.getMonth();
        LocalDate m = (month != null && !month.isEmpty()) ? LocalDate.of(Integer.parseInt(month.split("-")[0]),
                Integer.parseInt(month.split("-")[1]), 1) : null;
        LocalDate m2 = (m != null) ? m.plusMonths(1).minusDays(1) : null;

        String date = filters.getDatetime();
        LocalDate date_from = null;
        LocalDate date_to = null;
        if(date != null && !date.isEmpty()) {
            date_from = LocalDate.parse(date.split(" to ")[0]);
            date_to = LocalDate.parse(date.split(" to ")[1]);
        }

        return Specification.where(InvoiceSpecifications.hasId(id)
                .and(InvoiceSpecifications.hasStatus(status))
                .and(InvoiceSpecifications.hasApartmentNumber(apartment))
                .and(InvoiceSpecifications.hasOwner(owner))
                .and(InvoiceSpecifications.isCompleted(completed))
                .and(InvoiceSpecifications.datesBetween(date_from, date_to))
                .and(InvoiceSpecifications.datesBetween(m, m2)));
    }

    public Long getFilteredInvoiceCount(FilterForm filters) {

        Specification<Invoice> spec = buildSpecFromFilters(filters);

        return invoiceRepository.count(spec);
    }

    public Double getAverageTotalPriceForApartmentLastYear(Long apartment) {
        LocalDate lastYear = LocalDate.now().minusYears(1);
        LocalDate today = LocalDate.now();
        return invoiceRepository.getAverageTotalPriceForApartmentBetwenDate(apartment, lastYear, today);
    }



    public String turnInvoiceIntoExcel(Invoice invoice, InvoiceTemplate template) throws IOException {

        return excelHelper.turnInvoiceIntoExcel(invoice, template);

    }

    public void insertService(Invoice invoice, Sheet sheet, Row row) {

        List<InvoiceComponents> componentsList = invoice.getComponents();
        for (int i = 0; i < componentsList.size(); i++) {
            Row newRow = sheet.createRow(row.getRowNum()+i+1);
            InvoiceComponents component = componentsList.get(i);
            newRow.createCell(0).setCellValue(component.getService().getName());
            newRow.createCell(1).setCellValue(invoice.getTariff().getName());
            newRow.createCell(2).setCellValue(component.getService().getUnit().getName());
            newRow.createCell(3).setCellValue(component.getUnit_amount());
            newRow.createCell(4).setCellValue(component.getTotalPrice());
        }
    }

    public void sendExcelInvoiceToEmail() {

    }

}
