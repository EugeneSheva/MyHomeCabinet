package com.example.myhome.home.service;

import com.example.myhome.home.model.*;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.InvoiceComponentRepository;
import com.example.myhome.home.repository.InvoiceRepository;
import com.example.myhome.home.repository.InvoiceTemplateRepository;
import com.example.myhome.home.repository.PaymentDetailsRepository;
import com.example.myhome.home.repository.specifications.InvoiceSpecifications;
import lombok.extern.java.Log;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Invoice> findAllBySpecificationAndPage(FilterForm filters, Integer page, Integer page_size) {
        log.info("Filters found!");
        log.info(filters.toString());

        Specification<Invoice> specification = buildSpecFromFilters(filters);

        Pageable pageable = PageRequest.of(page, page_size);

        return invoiceRepository.findAll(specification, pageable).toList();
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
        invoice.setDate(LocalDate.parse(date));
        Invoice savedInvoice = saveInvoice(invoice);

        savedInvoice = buildComponents(savedInvoice, services, unit_prices, unit_amounts);

        return saveInvoice(savedInvoice);
    }

    public Invoice buildComponents(Invoice invoice,
                                   String[] services,
                                   String[] unit_prices,
                                   String[] unit_amounts) {


        for (int i = 1; i < services.length; i++) {
            InvoiceComponents component = new InvoiceComponents();
            component.setInvoice(invoice);
            component.setService(serviceService.findServiceById(Long.parseLong(services[i])));
            component.setUnit_price(Double.parseDouble(unit_prices[i]));
            component.setUnit_amount(Double.parseDouble(unit_amounts[i]));
            invoice.getComponents().add(component);
        }
        Double total_price = invoice.getComponents().stream().map(InvoiceComponents::getTotalPrice).reduce(Double::sum).orElse(0.0);
        invoice.setTotal_price(total_price);

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

        String date = filters.getDate();
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


    public String turnInvoiceIntoExcel(Invoice invoice, InvoiceTemplate template) throws IOException {

        log.info(invoice.toString());

        PaymentDetails pd = paymentDetailsRepository.findById(1L).orElse(null);
        String paymentDetails = (pd != null) ? pd.getName() + ", " + pd.getDescription() : "N/A";
        String invoiceOwnerName = (invoice.getApartment() != null && invoice.getApartment().getOwner() != null) ?
                invoice.getApartment().getOwner().getFullName() : "N/A";
        String invoiceTotalPrice = String.valueOf(invoice.getTotal_price());
        String invoiceAccountNumber = (invoice.getAccount() != null) ? String.format("%010d", invoice.getAccount().getId()) : "N/A";
        String invoiceAccountBalance = (invoice.getAccount() != null) ? String.valueOf(invoice.getAccount().getBalance()) : "N/A";
        String invoiceID = String.valueOf(invoice.getId());
        String invoiceDate = invoice.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String invoiceMonth = invoice.getDate().format(DateTimeFormatter.ofPattern("MMMM yyyy"));

        try(InputStream in = new FileInputStream(FILE_PATH+template.getFile())){
            Workbook workbook = WorkbookFactory.create(in);
            log.info(workbook.toString());
            Sheet sheet = workbook.getSheetAt(0);
            log.info(sheet.getSheetName());
            log.info(sheet.toString());

            for(Row row : sheet) {
                for(Cell cell : row) {
                    if(cell.getCellType().equals(CellType.STRING)) {
                        String param = cell.getStringCellValue();
                        switch(param) {
                            case "%paymentDetails%": cell.setCellValue(paymentDetails); break;
                            case "%invoiceOwnerName%": cell.setCellValue(invoiceOwnerName); break;
                            case "%invoiceTotalPrice%":
                            case "%total%": cell.setCellValue(invoiceTotalPrice); break;
                            case "%invoiceAccountNumber%": cell.setCellValue(invoiceAccountNumber); break;
                            case "%invoiceAccountBalance%": cell.setCellValue(invoiceAccountBalance); break;
                            case "%invoiceID%": cell.setCellValue(invoiceID); break;
                            case "%invoiceDate%": cell.setCellValue(invoiceDate); break;
                            case "%invoiceMonth%": cell.setCellValue(invoiceMonth); break;
//                            case "%services%": insertService(invoice, sheet, row); break;
                            default:
                        }
                    }
                }
            }
            try (OutputStream fileOut = new FileOutputStream(FILE_PATH+template.getFile())){
                workbook.write(fileOut);
            }
            log.info("Workbook closed");
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("try block over");
        return template.getFile();
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
