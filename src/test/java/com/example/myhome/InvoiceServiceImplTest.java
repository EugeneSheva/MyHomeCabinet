package com.example.myhome;

import com.example.myhome.home.model.Invoice;
import com.example.myhome.home.repository.InvoiceRepository;
import com.example.myhome.home.service.impl.InvoiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class InvoiceServiceImplTest {

    @MockBean private InvoiceRepository invoiceRepository;
    @Autowired private InvoiceServiceImpl invoiceServiceImpl;

    Invoice invoice;

    @BeforeEach
    void createInvoice() {
        invoice = new Invoice();
    }

    @Test
    void sanityCheck() {
        assertThat(invoiceRepository).isNotNull();
        assertThat(invoiceServiceImpl).isNotNull();
        assertThat(invoice).isNotNull();
    }
}
