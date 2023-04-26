package com.example.myhome.home.service;

import com.example.myhome.home.model.Invoice;
import com.example.myhome.home.model.filter.FilterForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface InvoiceService {

    List<Invoice> findAllInvoices();
    Page<Invoice> findAllInvoicesByFiltersAndPage(FilterForm filters, Pageable pageable);

    Invoice findInvoiceById(Long invoice_id);

    Invoice saveInvoice(Invoice invoice);

    List<Invoice> saveAllInvoices(List<Invoice> list);

    Long getMaxInvoiceId();

    void deleteInvoiceById(Long invoice_id);

    Specification<Invoice> buildSpecFromFilters(FilterForm filters);


}
