package com.example.myhome.home.service;

import com.example.myhome.home.model.IncomeExpenseType;
import com.example.myhome.home.model.Invoice;
import com.example.myhome.home.model.InvoiceComponents;
import com.example.myhome.home.model.InvoiceStatus;
import com.example.myhome.home.repository.InvoiceComponentRepository;
import com.example.myhome.home.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceComponentRepository invoiceComponentRepository;

    @Autowired
    private ServiceService serviceService;

    public List<Invoice> findAllInvoices() {return invoiceRepository.findAll();}

    public Invoice findInvoiceById(long invoice_id) {return invoiceRepository.findById(invoice_id).orElseThrow();}

    public Long getMaxInvoiceId() {return invoiceRepository.getMaxId().orElse(0L);}

    public Invoice saveInvoice(Invoice invoice) {return invoiceRepository.save(invoice);}

    public List<Invoice> saveAllInvoices(List<Invoice> list) {return invoiceRepository.saveAll(list);}
    public List<InvoiceComponents> saveAllInvoicesComponents(List<InvoiceComponents> list) {return invoiceComponentRepository.saveAll(list);}

    public void deleteInvoiceById(long invoice_id) {
        Invoice invoice = findInvoiceById(invoice_id);
        invoiceComponentRepository.deleteAll(invoice.getComponents());
//        invoice.removeAllChildren();
        invoiceRepository.delete(invoice);
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
