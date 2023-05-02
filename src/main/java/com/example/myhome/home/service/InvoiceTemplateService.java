package com.example.myhome.home.service;

import com.example.myhome.home.model.Invoice;
import com.example.myhome.home.model.InvoiceTemplate;
import com.example.myhome.home.model.filter.FilterForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface InvoiceTemplateService {

    List<InvoiceTemplate> findAllTemplates();
    InvoiceTemplate findDefaultTemplate();

    InvoiceTemplate findTemplateById(Long template_id);

    void setDefaultTemplate(InvoiceTemplate template);

    InvoiceTemplate saveTemplate(InvoiceTemplate template);

    void deleteTemplateById(Long template_id);

    List<InvoiceTemplate> saveAllTemplates(List<InvoiceTemplate> list);
    public Page<Invoice> findAllInvoicesByFiltersAndPage(FilterForm filters, Pageable pageable);
    public Invoice findInvoiceById(Long invoice_id);

    public void deleteInvoiceById(Long invoice_id);

}
