package com.example.myhome.home.service;

import com.example.myhome.home.dto.InvoiceDTO;
import com.example.myhome.home.model.Invoice;
import com.example.myhome.home.model.InvoiceComponents;
import com.example.myhome.home.model.InvoiceTemplate;
import com.example.myhome.home.model.Owner;
import com.example.myhome.home.model.filter.FilterForm;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

public interface InvoiceService {

    Page<InvoiceDTO> findAllBySpecificationAndPageCabinet(FilterForm filters, Integer page, Integer size, Owner owner);



    Invoice findInvoiceById(Long invoice_id);




    List<String> getListOfMonthName();

    List<Double> getListExpenseByApartmentByMonth(Long id);

    Double getAverageTotalPriceForApartmentLastYear(Long apartment);



}
