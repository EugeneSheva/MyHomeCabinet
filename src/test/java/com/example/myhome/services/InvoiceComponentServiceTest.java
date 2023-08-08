package com.example.myhome.services;

import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.model.Invoice;
import com.example.myhome.home.model.InvoiceComponents;
import com.example.myhome.home.model.Service;
import com.example.myhome.home.repository.InvoiceComponentRepository;
import com.example.myhome.home.service.impl.InvoiceComponentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class InvoiceComponentServicelTest {

    @Mock
    private InvoiceComponentRepository invoiceComponentRepository;

    @InjectMocks
    private InvoiceComponentServiceImpl invoiceComponentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindExpensesLastMonthByApartment() {
        // Mock data
        Long apartmentId = 1L;
        Integer year = LocalDate.now().getYear();
        Integer lastMonthValue = LocalDate.now().minusMonths(1).getMonthValue();
        Integer lastDayOfMonth =  LocalDate.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
        LocalDate from = LocalDate.of(year, lastMonthValue, 01);
        LocalDate to = LocalDate.of(year, lastMonthValue, lastDayOfMonth);

        List<InvoiceComponents> mockInvoiceComponentsList = new ArrayList<>();
        mockInvoiceComponentsList.add(new InvoiceComponents(1L, new Invoice(1L, LocalDate.now().minusMonths(1), new Apartment(apartmentId, apartmentId), 100.0, from,to), new Service(1L,"Service1"), 50.0,1));
        mockInvoiceComponentsList.add(new InvoiceComponents(2L, new Invoice(2L, LocalDate.now().minusMonths(1), new Apartment(apartmentId, apartmentId), 200.0, from,to), new Service(2L, "Service2"), 70.0,1));
        when(invoiceComponentRepository.findByInvoice_Apartment_IdAndInvoice_DateBetween(apartmentId, LocalDate.now().minusMonths(1).withDayOfMonth(1), LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth())))
                .thenReturn(mockInvoiceComponentsList);

        System.out.println("mockInvoiceComponentsList " + mockInvoiceComponentsList);
        // Expected result
        Map<String, Double> expectedResults = new HashMap<>();
        expectedResults.put("Service1", 50.0);
        expectedResults.put("Service2", 70.0);

        // Test
        Map<String, Double> actualResults = invoiceComponentService.findExprncesLastMonthByApartment(apartmentId);
        System.out.println("actualResults " + actualResults);
        System.out.println("expectedResults " + expectedResults);
        assertEquals(expectedResults, actualResults);
    }

    @Test
    public void testFindExpensesThisYearByApartment() {
        // Mock data
        Long apartmentId = 1L;
        List<InvoiceComponents> mockInvoiceComponentsList = new ArrayList<>();
        mockInvoiceComponentsList.add(new InvoiceComponents(1L, new Invoice(1L, LocalDate.now().minusMonths(6), new Apartment(apartmentId,apartmentId), 100.0, LocalDate.now().minusMonths(1),LocalDate.now().minusMonths(1)), new Service(1L, "Service1"), 50.0, 1));
        mockInvoiceComponentsList.add(new InvoiceComponents(2L, new Invoice(2L, LocalDate.now().minusMonths(3), new Apartment(apartmentId, apartmentId), 200.0, LocalDate.now().minusMonths(1), LocalDate.now().minusMonths(1)), new Service(2L, "Service2"), 70.0, 1));
        when(invoiceComponentRepository.findByInvoice_Apartment_IdAndInvoice_DateBetween(apartmentId, LocalDate.now().withDayOfYear(1), LocalDate.now()))
                .thenReturn(mockInvoiceComponentsList);

        // Expected result
        Map<String, Double> expectedResults = new HashMap<>();
        expectedResults.put("Service1", 50.0);
        expectedResults.put("Service2", 70.0);

        // Test
        Map<String, Double> actualResults = invoiceComponentService.findExprncesThisYearByApartment(apartmentId);
        assertEquals(expectedResults, actualResults);
    }
}