package com.example.myhome;

import com.example.myhome.home.service.*;
import com.example.myhome.home.service.impl.AccountServiceImpl;
import com.example.myhome.home.service.impl.InvoiceServiceImpl;
import com.example.myhome.home.service.impl.TariffServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MyHomeApplicationTests {

    @MockBean
    private AccountServiceImpl accountServiceImpl;
    @MockBean
    private AdminService adminService;
    @MockBean
    private ApartmentService apartmentService;
    @MockBean
    private BuildingService buildingService;
    @MockBean
    private InvoiceServiceImpl invoiceServiceImpl;
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
    private TariffServiceImpl tariffService;

    @Test
    void contextLoads() {
        assertThat(accountServiceImpl).isNotNull();
        assertThat(apartmentService).isNotNull();
        assertThat(adminService).isNotNull();
        assertThat(buildingService).isNotNull();
        assertThat(invoiceServiceImpl).isNotNull();
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
