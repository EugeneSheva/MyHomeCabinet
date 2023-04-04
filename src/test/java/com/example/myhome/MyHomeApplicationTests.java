package com.example.myhome;

import com.example.myhome.home.service.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MyHomeApplicationTests {

    @MockBean
    private AccountService accountService;
    @MockBean
    private AdminService adminService;
    @MockBean
    private ApartmentService apartmentService;
    @MockBean
    private BuildingService buildingService;
    @MockBean
    private InvoiceService invoiceService;
    @MockBean
    private MessageService messageService;
    @MockBean
    private MeterDataService meterDataService;
    @MockBean
    private OwnerService ownerService;
    @MockBean
    private RepairRequestService repairRequestService;
    @MockBean
    private ServiceService serviceService;
    @MockBean
    private TariffService tariffService;

    @Test
    void contextLoads() {
        assertThat(accountService).isNotNull();
        assertThat(apartmentService).isNotNull();
        assertThat(adminService).isNotNull();
        assertThat(buildingService).isNotNull();
        assertThat(invoiceService).isNotNull();
        assertThat(messageService).isNotNull();
        assertThat(meterDataService).isNotNull();
        assertThat(ownerService).isNotNull();
        assertThat(repairRequestService).isNotNull();
        assertThat(serviceService).isNotNull();
        assertThat(tariffService).isNotNull();
    }

    @Test
    void saveTariff() {

    }

}
