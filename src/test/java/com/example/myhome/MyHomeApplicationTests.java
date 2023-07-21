package com.example.myhome;

import com.example.myhome.home.service.*;
import com.example.myhome.home.service.impl.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MyHomeApplicationTests {


    @MockBean
    private AdminServiceImpl adminService;
    @MockBean
    private ApartmentServiceImpl apartmentService;

    @MockBean
    private InvoiceService invoiceService;
    @MockBean
    private MessageService messageService;

    @MockBean
    private OwnerService ownerService;
    @MockBean
    private RepairRequestService repairRequestService;


    @Test
    void contextLoads() {
        assertThat(apartmentService).isNotNull();
        assertThat(adminService).isNotNull();
        assertThat(invoiceService).isNotNull();
        assertThat(messageService).isNotNull();
        assertThat(ownerService).isNotNull();
        assertThat(repairRequestService).isNotNull();

    }

}
