package com.example.myhome.home.controller;

import com.example.myhome.config.TestConfig;
import com.example.myhome.home.dto.*;
import com.example.myhome.home.mapper.AdminDTOMapper;

import com.example.myhome.home.mapper.ApartmentDTOMapper;
import com.example.myhome.home.mapper.OwnerDTOMapper;
import com.example.myhome.home.model.*;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.*;
import com.example.myhome.home.service.*;
import com.example.myhome.home.service.impl.AdminServiceImpl;
import com.example.myhome.home.service.impl.ApartmentServiceImpl;
import com.example.myhome.home.service.impl.InvoiceComponentServiceImpl;
import com.example.myhome.home.service.registration.VerificationTokenRepository;
import com.example.myhome.home.validator.OwnerValidator;
import com.example.myhome.home.validator.RequestValidator;
import com.example.myhome.util.UserStatus;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.security.Principal;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@WebMvcTest(PersonalCabinetController.class)
@ComponentScan("com.example.myhome.home.mapper")
class PersonalCabinetControllerTest {
    @MockBean
    Principal principal;
    @Autowired
    GlobalControllerAdvice globalControllerAdvice;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AdminDTOMapper adminDTOMapper;
    @MockBean
    PageRoleDisplayRepository pageRoleDisplayRepository;
    @MockBean
    private OwnerService ownerService;
    @MockBean
    private AdminServiceImpl adminService;
    @MockBean
    private AdminRepository adminRepository;
    @MockBean
    private UserRoleRepository userRoleRepository;
    @MockBean
    private PersistentTokenRepository repository;
    @MockBean
    private PersistentTokenBasedRememberMeServices rememberMeServices;
    @MockBean
    private ApartmentServiceImpl apartmentService;
    @MockBean
    private InvoiceService invoiceService;
    @MockBean
    private InvoiceComponentServiceImpl invoiceComponentService;
    @MockBean
    private MessageService messageService;
    @MockBean
    private MessageRepository messageRepository;
    @MockBean
    private RepairRequestRepository repairRequestRepository;
    @MockBean
    private RepairRequestService repairRequestService;
    @MockBean
    private OwnerValidator ownerValidator;
//    @Autowired
//    private static OwnerDTOMapper ownerDTOMapper;
//    @Autowired
//    private static ApartmentDTOMapper apartmentDTOMapper;
    @MockBean
    private RequestValidator validator;
    @MockBean
    private MessageSource messageSource;
    @MockBean
    private TariffRepository tariffRepository;
    @MockBean
    private IncomeExpenseRepository incomeExpenseRepository;
    @MockBean
    private InvoiceTemplateRepository invoiceTemplateRepository;
    @MockBean
    private BuildingRepository buildingRepository;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    DocumentRepository documentRepository;
    @MockBean
    MeterDataRepository meterDataRepository;
    @MockBean
    InvoiceComponentRepository invoiceComponentRepository;
    @MockBean
    OwnerRepository ownerRepository;
    @MockBean
    ApartmentRepository apartmentRepository;
    @MockBean
    InvoiceRepository invoiceRepository;
    @MockBean
    ServiceRepository serviceRepository;
    @MockBean
    CashBoxRepository cashBoxRepository;
    @MockBean
    PaymentDetailsRepository paymentDetailsRepository;
    @MockBean
    PageRepository pageRepository;
    @MockBean
    VerificationTokenRepository verificationTokenRepository;
    @MockBean
    UnitRepository unitRepository;
    @InjectMocks
    private PersonalCabinetController personalCabinetController;
    static OwnerDTO owner;
    static OwnerDTO ownerDTO;
    static List<Apartment>apartmentList = new ArrayList<>();
    static List<ApartmentDTO>apartmentDTOList = new ArrayList<>();
    static BuildingDTO building;
    static ApartmentDTO apartment;
    static ApartmentDTO apartmentDTO;
    @BeforeAll
    static void setupObjects() {
        building = new BuildingDTO();
        building.setName("TestBuilding");
        building.setAddress("TestBuildingAddress");
        building.setSections(List.of("TestBuildingSection1", "TestBuildingSection2"));
        building.setFloors(List.of("TestBuildingFloor1", "TestBuildingFloor2"));
        building.setApartments(new ArrayList<>());

        apartment = new ApartmentDTO();
        apartment.setNumber(1L);
        apartment.setBalance(100.0);
        apartment.setFloor("TestBuildingFloor1");
        apartment.setSection("TestBuildingSection1");
        apartment.setBuilding(building);
//        apartment.setOwner(ownerRepository.findById(1L).orElseThrow());
//        apartment.setTariff(tariffRepository.findById(1L).orElseThrow());
        apartment.setSquare(111.0);
        building.getApartments().add(apartment);
        apartmentDTOList.add(apartment);


        apartmentDTOList.add(apartmentDTO);


        owner = new OwnerDTO();
        owner.setId(1L);
        owner.setFirst_name("Owner");
        owner.setFathers_name("For");
        owner.setLast_name("Testing");
        owner.setPassword("12345678");
        owner.setEmail("user@example.com");
        owner.setDescription("test");
        owner.setAdded_at(LocalDateTime.now());
//        owner.setEnabled(true);
        owner.setStatus("ACTIVE");
///
        owner.setViber("test");
        owner.setBirthdate(LocalDate.now());
        owner.setApartments(apartmentDTOList);
        owner.setPhone_number("0501111111");
        owner.setDescription("description");
        owner.setViber(owner.getPhone_number());
        owner.setTelegram(owner.getPhone_number());
        owner.setBirthdate(LocalDate.of(2000,11,11));

    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(personalCabinetController).build();
//        when(ownerService.findOwnerDTObyEmail(principal.getName())).thenReturn(ownerDTO);
//        when(principal.getName()).thenReturn("user@example.com");
    }


    @Test
    @WithMockUser(username = "user@example.com", password = "12345678")
    public void testGetStartPage() throws Exception {
        when(principal.getName()).thenReturn("user@example.com");
        when(ownerService.findOwnerDTObyEmail(principal.getName())).thenReturn(owner);
        System.out.println("ownerDTO"+ owner);
        System.out.println("apartmentDTOList"+apartmentDTOList);
        when(ownerDTO.getApartments()).thenReturn(apartmentDTOList);
        System.out.println("!!!mockMvc " + mockMvc.perform(MockMvcRequestBuilders.get("/cabinet")));
        mockMvc.perform(MockMvcRequestBuilders.get("/cabinet"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("cabinet/index"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("owner", "apartment", "apartmentBalance", "avgInvoicePriceInMonth", "byMonthNames", "byMonthValues", "byYearName", "byYearValue", "monthsName", "apartExpenseEachMonthByYear", "indexPageActive", "apartmentId"));
    }


}