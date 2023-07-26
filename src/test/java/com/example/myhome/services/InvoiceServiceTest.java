package com.example.myhome.services;

import com.example.myhome.home.dto.InvoiceDTO;
import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.mapper.InvoiceDTOMapper;
import com.example.myhome.home.model.*;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.*;
import com.example.myhome.home.service.InvoiceService;

import com.example.myhome.util.ExcelHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class InvoiceServiceTest {

    @MockBean private InvoiceRepository invoiceRepository;
    @MockBean private InvoiceTemplateRepository invoiceTemplateRepository;
    @MockBean private AccountRepository accountRepository;
    @MockBean private ApartmentRepository apartmentRepository;
    @MockBean private OwnerRepository ownerRepository;
    @MockBean private BuildingRepository buildingRepository;
    @Autowired private InvoiceService invoiceService;

    @MockBean private ExcelHelper helper;

    Invoice invoice;
    List<Invoice> list;
    InvoiceDTOMapper mapper = new InvoiceDTOMapper();

    @BeforeEach
    void createInvoice() {
        invoice = new Invoice();
        invoice.setId(1L);
        invoice.setCompleted(true);
        invoice.setDate(LocalDate.now());
        invoice.setComponents(new ArrayList<>());
        invoice.setTotal_price(0);
        Apartment apartment = new Apartment();
        apartment.setId(1L);
        Building building = new Building();
        building.setId(1L);
        apartment.setBuilding(building);
        Owner owner = new Owner();
        owner.setId(1L);
        apartment.setOwner(owner);
        invoice.setApartment(apartment);
        invoice.setOwner(owner);
        invoice.setSection("test");
        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setBuilding(building);

        list = List.of(invoice,invoice,invoice);
    }

    @Test
    void sanityCheck() {
        assertThat(invoiceRepository).isNotNull();
        assertThat(invoiceService).isNotNull();
        assertThat(invoice).isNotNull();
    }


    @Test
    void canFindInvoiceByIdTest() {
        Invoice expected = new Invoice();
        expected.setId(1L);
        when(invoiceRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        assertThat(invoiceService.findInvoiceById(1L)).isEqualTo(expected);
    }

    @Test
    void canThrowExceptionOnInvoiceNotFoundTest() {
        when(invoiceRepository.findById(anyLong())).thenThrow(new NotFoundException());
        assertThat(invoiceService.findInvoiceById(1L)).isNull();
    }





    @Test
    void canGetListOfMonthNameTest() {
        assertThat(invoiceService.getListOfMonthName().size()).isEqualTo(12);
    }

    @Test
    void canGetListExpenseByApartmentByMonthTest() {
        assertThat(invoiceService.getListExpenseByApartmentByMonth(1L).size()).isEqualTo(12);
    }

    @Test
    void canAverageTotalPriceForApartmentLastYearTest() {
        when(invoiceRepository.getAverageTotalPriceForApartmentBetwenDate(any(), any(),any())).thenReturn(12.0);
        assertThat(invoiceService.getAverageTotalPriceForApartmentLastYear(1L)).isEqualTo(12.0);
    }




}
