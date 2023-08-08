package com.example.myhome.services;

import com.example.myhome.home.dto.ApartmentDTO;
import com.example.myhome.home.dto.BuildingDTO;
import com.example.myhome.home.dto.InvoiceDTO;
import com.example.myhome.home.dto.OwnerDTO;
import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.mapper.InvoiceDTOMapper;
import com.example.myhome.home.model.*;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.*;
import com.example.myhome.home.service.InvoiceService;

import com.example.myhome.util.ExcelHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.myhome.home.model.InvoiceStatus.PAID;
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
    @Mock InvoiceDTOMapper mapper;
    Invoice invoice;
    List<Invoice> list;


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
        invoice.setStatus(PAID);
        invoice.setBuilding(building);

        list = List.of(invoice,invoice,invoice);
    }
    @Test
    void canFindAllInvoicesBySpecificationAndPageCabinet() {
        // Given
        FilterForm filters = new FilterForm();
        filters.setDate("2023-07-31");
        filters.setStatus("PAID");
        filters.setApartment(1L);
        Owner owner = new Owner();

        // Mock the invoiceRepository response
        Pageable pageable = Pageable.ofSize(10);
        List<Invoice> invoiceList = new ArrayList<>();
        Invoice invoice1 = new Invoice();
        invoice1.setId(1L);
        invoice1.setDate(LocalDate.parse("2023-07-31", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        invoice1.setStatus(PAID);
        invoice1.setApartment(new Apartment(1L,1L, new Building(1L, "test"), new Owner(1L)));
        invoice1.setOwner(new Owner(1L));
        invoice1.setBuilding(new Building(1L, "test"));
        invoiceList.add(invoice1);
        Page<Invoice> pageResult = new PageImpl<>(invoiceList, pageable, 1);
        when(invoiceRepository.findByFilters(any(), any(), any(), any(), any())).thenReturn(pageResult);

        // Mock the mapper response
        InvoiceDTO invoiceDTO1 = new InvoiceDTO();
        invoiceDTO1.setId(1L);
        invoiceDTO1.setDate(LocalDate.parse("2023-07-31", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        invoiceDTO1.setStatus(InvoiceStatus.valueOf("PAID"));
        invoiceDTO1.setApartment(new ApartmentDTO());
        invoiceDTO1.setOwner(new OwnerDTO());
        invoiceDTO1.setBuilding(new BuildingDTO());
        when(mapper.fromInvoiceToDTO(invoice1)).thenReturn(invoiceDTO1);

        // When
        Page<InvoiceDTO> resultPage = invoiceService.findAllBySpecificationAndPageCabinet(filters, 1, 10, owner);

        // Then
        assertThat(resultPage).isNotNull();
        assertThat(resultPage.getContent()).hasSize(1);
        assertThat(resultPage.getContent().get(0).getId()).isEqualTo(1L);
        assertThat(resultPage.getContent().get(0).getDate()).isEqualTo(LocalDate.parse("2023-07-31", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        assertThat(resultPage.getContent().get(0).getStatus()).isEqualTo(PAID);
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
