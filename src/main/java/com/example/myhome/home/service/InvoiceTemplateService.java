package com.example.myhome.home.service;

import com.example.myhome.home.model.InvoiceTemplate;

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

}
