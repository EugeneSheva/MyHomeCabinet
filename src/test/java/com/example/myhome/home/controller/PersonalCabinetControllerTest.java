package com.example.myhome.home.controller;

import com.example.myhome.home.dto.*;
import com.example.myhome.home.mapper.AdminDTOMapper;
import com.example.myhome.home.mapper.OwnerDTOMapper;
import com.example.myhome.home.model.*;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.*;
import com.example.myhome.home.service.InvoiceService;
import com.example.myhome.home.service.MessageService;
import com.example.myhome.home.service.OwnerService;
import com.example.myhome.home.service.RepairRequestService;
import com.example.myhome.home.service.impl.AdminServiceImpl;
import com.example.myhome.home.service.impl.ApartmentServiceImpl;
import com.example.myhome.home.service.impl.InvoiceComponentServiceImpl;
import com.example.myhome.home.service.registration.VerificationTokenRepository;
import com.example.myhome.home.validator.OwnerValidator;
import com.example.myhome.home.validator.RequestValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonalCabinetController.class)
@ComponentScan("com.example.myhome.home.mapper")
class PersonalCabinetControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    GlobalControllerAdvice globalControllerAdvice;
    @Autowired
    private MockMvc mockMvc;



    @MockBean
    Principal principal;
    @MockBean
    private AdminDTOMapper adminDTOMapper;
    @MockBean
    private OwnerDTOMapper ownerDTOMapper;
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
    private DocumentRepository documentRepository;
    @MockBean
    private MeterDataRepository meterDataRepository;
    @MockBean
    private InvoiceComponentRepository invoiceComponentRepository;
    @MockBean
    private OwnerRepository ownerRepository;
    @MockBean
    private ApartmentRepository apartmentRepository;
    @MockBean
    private InvoiceRepository invoiceRepository;
    @MockBean
    private ServiceRepository serviceRepository;
    @MockBean
    private CashBoxRepository cashBoxRepository;
    @MockBean
    private PaymentDetailsRepository paymentDetailsRepository;
    @MockBean
    private PageRepository pageRepository;
    @MockBean
    private VerificationTokenRepository verificationTokenRepository;
    @MockBean
    private UnitRepository unitRepository;
    @Mock
    private Principal mockPrincipal;
    @InjectMocks
    private PersonalCabinetController personalCabinetController;

    static OwnerDTO ownerDTO;
    static Owner owner;
    static List<Apartment> apartmentList = new ArrayList<>();
    static List<ApartmentDTO> apartmentDTOList = new ArrayList<>();
    static BuildingDTO buildingDTO;
    static Building building;
    static Apartment apartment;
    static ApartmentDTO apartmentDTO;
    static InvoiceDTO invoiceDTO;
    static Tariff tariff;
    static List<Message> messagesList;
    static Message message1;
    static List<Message> unreadMessages;
    static Message message2;
    static List<Owner> unreadReceivers;
    static List<Owner> receivers;
    static Admin admin;
    static ApartmentAccountDTO accountDTO;
    static List<InvoiceDTO> invoiceList = new ArrayList<>();
    static Page<InvoiceDTO> invoices;
    static InvoiceDTO invoiceDTO1;
    static InvoiceDTO invoiceDTO2;
    static FilterForm form = new FilterForm();

    @BeforeAll
    static void setupObjects() {

        buildingDTO = new BuildingDTO();
        buildingDTO.setId(1L);
        buildingDTO.setName("TestBuilding");
        buildingDTO.setAddress("TestBuildingAddress");
        buildingDTO.setSections(List.of("TestBuildingSection1", "TestBuildingSection2"));
        buildingDTO.setFloors(List.of("TestBuildingFloor1", "TestBuildingFloor2"));
        buildingDTO.setApartments(new ArrayList<>());

        building = new Building();
        building.setId(1L);
        building.setName("TestBuilding");
        building.setAddress("TestBuildingAddress");
        building.setSections(List.of("TestBuildingSection1", "TestBuildingSection2"));
        building.setFloors(List.of("TestBuildingFloor1", "TestBuildingFloor2"));
        building.setApartments(new ArrayList<>());

        apartmentDTO = new ApartmentDTO();
        apartmentDTO.setId(1L);
        apartmentDTO.setNumber(1L);
        apartmentDTO.setBalance(100.0);
        apartmentDTO.setFloor("TestBuildingFloor1");
        apartmentDTO.setSection("TestBuildingSection1");
        apartmentDTO.setBuilding(buildingDTO);
        apartmentDTO.setSquare(111.0);
        buildingDTO.getApartments().add(apartmentDTO);
        apartmentDTOList.add(apartmentDTO);

        apartment = new Apartment();
        apartment.setId(1L);
        apartment.setNumber(1L);
        apartment.setBalance(100.0);
        apartment.setFloor("TestBuildingFloor1");
        apartment.setSection("TestBuildingSection1");
        apartment.setBuilding(building);
        apartment.setSquare(111.0);
        building.getApartments().add(apartment);
        apartmentList.add(apartment);

        accountDTO = new ApartmentAccountDTO();
        accountDTO.setId(1L);
        accountDTO.setIsActive(true);
        accountDTO.setApartment(apartmentDTO);
        accountDTO.setSection(apartmentDTO.getSection());
        accountDTO.setBuilding(apartmentDTO.getBuilding());

        apartmentDTO.setAccount(accountDTO);

        ownerDTO = new OwnerDTO();
        ownerDTO.setId(1L);
        ownerDTO.setFirst_name("Owner");
        ownerDTO.setFathers_name("For");
        ownerDTO.setLast_name("Testing");
        ownerDTO.setPassword("12345678");
        ownerDTO.setEmail("user@example.com");
        ownerDTO.setDescription("test");
        ownerDTO.setAdded_at(LocalDateTime.now());
//        ownerDTO.setEnabled(true);
        ownerDTO.setStatus("ACTIVE");
///
        ownerDTO.setViber("test");
        ownerDTO.setBirthdate(LocalDate.now());
        ownerDTO.setApartments(apartmentDTOList);
        ownerDTO.setPhone_number("0501111111");
        ownerDTO.setDescription("description");
        ownerDTO.setViber("0501111111");
        ownerDTO.setTelegram("0501111111");
        ownerDTO.setBirthdate(LocalDate.of(2000, 11, 11));
        ownerDTO.setOldpassword("12345678");

        tariff = new Tariff();
        apartment.setTariff(tariff);

        unreadReceivers = new ArrayList<>();
        owner = new Owner();
        owner.setUnreadMessages(unreadMessages);
        unreadReceivers.add(owner);
        owner.setOldpassword("12345678");


        receivers = new ArrayList<>();
        receivers.add(owner);

        messagesList = new ArrayList<>();
        message1 = new Message();
        message1.setId(1L);
        message1.setSubject("Message 1");
        message1.setReceivers(receivers);
        message1.setUnreadReceivers(unreadReceivers);
        messagesList.add(message1);

        admin = new Admin();
        admin.setId(1L);
        admin.setFull_name("testAdmin");

        unreadMessages = new ArrayList<>();
        message2 = new Message();
        message2.setId(2L);
        message2.setSender(admin);
        message2.setSubject("Unread Message");
        message2.setReceivers(receivers);
        messagesList.add(message2);
        unreadMessages.add(message2);
        message2.setUnreadReceivers(unreadReceivers);
        owner.setMessages(messagesList);

        admin = new Admin();
        admin.setId(1L);
        admin.setFull_name("testAdmin");

        invoiceDTO1 = new InvoiceDTO();
        invoiceDTO1.setId(1L);
        invoiceList.add(invoiceDTO1);

        invoiceDTO2 = new InvoiceDTO();
        invoiceDTO2.setId(2L);
        invoiceList.add(invoiceDTO2);
        invoices = new PageImpl<>(invoiceList);

    }

//    @Before
//    public void setup() {
////        mockMvc = MockMvcBuilders.standaloneSetup(personalCabinetController).build();
////        when(ownerService.findOwnerDTObyEmail(principal.getName())).thenReturn(ownerDTO);
////        when(principal.getName()).thenReturn("user@example.com");
//    }



//      invoices


    @Test
    @WithMockUser(username = "user@example.com", password = "12345678")
    public void testGetInvoicePageByOwner() throws Exception {
        Mockito.when(ownerService.findOwnerDTObyEmail(Mockito.anyString()))
                .thenReturn(ownerDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/cabinet/invoices")

                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("invoicesPageActive"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("allInvoicesPageActive"))
                .andExpect(MockMvcResultMatchers.view().name("cabinet/invoices"))
                .andExpect(status().isOk());

        Mockito.verify(ownerService).findOwnerDTObyEmail("user@example.com");
    }

    @Test
    @WithMockUser(username = "user@example.com", password = "12345678")
    public void testGetInvoicePageByApartment() throws Exception {
        Mockito.when(ownerService.findOwnerDTObyEmail(Mockito.anyString()))
                .thenReturn(ownerDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/cabinet/invoices/{id}", 1)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
                .andExpect(MockMvcResultMatchers.model().attribute("owner", ownerDTO))
                .andExpect(MockMvcResultMatchers.model().attributeExists("invoicesPageActive"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("apartmentId"))
                .andExpect(MockMvcResultMatchers.model().attribute("apartmentId", 1L))
                .andExpect(MockMvcResultMatchers.view().name("cabinet/invoices"));

        Mockito.verify(ownerService).findOwnerDTObyEmail("user@example.com");
    }

    @Test
    @WithMockUser(username = "user@example.com", password = "12345678")
    public void getInvoices_shouldReturnPageOfInvoices() throws Exception {

        when(ownerService.findByLogin(anyString())).thenReturn(owner);
        when(invoiceService.findAllBySpecificationAndPageCabinet(eq(form), anyInt(), anyInt(), eq(owner))).thenReturn(invoices);

        mockMvc.perform(get("/cabinet/get-invoices-cabinet")
                        .param("page", "0")
                        .param("size", "10")
                        .param("filters", objectMapper.writeValueAsString(form))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
    }


//      tariffs


    @Test
    @WithMockUser(username = "user@example.com", password = "12345678")
    public void testGetTariffsPage() throws Exception {

        Mockito.when(ownerService.findOwnerDTObyEmail(Mockito.anyString()))
                .thenReturn(ownerDTO);
        Mockito.when(apartmentService.findApartmentDto(Mockito.anyLong()))
                .thenReturn(apartmentDTO);
        Mockito.when(apartmentService.findById(Mockito.anyLong()))
                .thenReturn(apartment);

        mockMvc.perform(MockMvcRequestBuilders.get("/cabinet/tariffs/{id}", 1L)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
                .andExpect(MockMvcResultMatchers.model().attribute("owner", ownerDTO))
                .andExpect(MockMvcResultMatchers.model().attributeExists("apart"))
                .andExpect(MockMvcResultMatchers.model().attribute("apart", apartmentDTO))
                .andExpect(MockMvcResultMatchers.model().attributeExists("tariff"))
                .andExpect(MockMvcResultMatchers.model().attribute("tariff", tariff))
                .andExpect(MockMvcResultMatchers.model().attributeExists("tariffsPageActive"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("apartmentId"))
                .andExpect(MockMvcResultMatchers.model().attribute("apartmentId", 1L))
                .andExpect(MockMvcResultMatchers.view().name("cabinet/tariffs"));

        Mockito.verify(ownerService).findOwnerDTObyEmail("user@example.com");
        Mockito.verify(apartmentService).findApartmentDto(1L);
        Mockito.verify(apartmentService).findById(1L);
    }


//      messages


    @Test
    @WithMockUser(username = "user@example.com", password = "12345678")
    public void testGetMessagesPage() throws Exception {

        when(ownerService.findOwnerDTObyEmail(anyString())).thenReturn(ownerDTO);
        when(ownerService.findOwnerDTObyEmailWithMessages(anyString())).thenReturn(ownerDTO);
        when(ownerService.findByLogin(anyString())).thenReturn(owner);


        mockMvc.perform(get("/cabinet/messages")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("cabinet/messages"))
                .andExpect(model().attributeExists("owner"))
                .andExpect(model().attributeExists("unreadMessagesId"))
                .andExpect(model().attributeExists("filterForm"))
                .andExpect(model().attributeExists("messagesPageActive"))
                .andExpect(model().attribute("unreadMessagesId", hasSize(1)));

        Mockito.verify(ownerService).findOwnerDTObyEmail("user@example.com");

    }

    @Test
    @WithMockUser(username = "user@example.com", password = "12345678")
    public void testGetMessageContentPage() throws Exception {

        when(ownerService.findOwnerDTObyEmail(anyString())).thenReturn(ownerDTO);
        when(messageRepository.getReferenceById(anyLong())).thenReturn(message2);
        when(ownerService.findByLogin(anyString())).thenReturn(owner);

        mockMvc.perform(get("/cabinet/messages/{id}", 1)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("cabinet/message_card"))
                .andExpect(model().attributeExists("owner"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attributeExists("messagesPageActive"))
                .andExpect(model().attribute("message", equalTo(message2)))
                .andExpect(model().attribute("message", hasProperty("unreadReceivers", not(contains(owner)))));
    }

    @Test
    @WithMockUser(username = "user@example.com", password = "12345678")
    public void getUnreadMessages_shouldReturnListOfUnreadMessageIds() throws Exception {
        List<Message>unreadMessages = new ArrayList<>();
        unreadMessages.add(message1);
        unreadMessages.add(message2);
        Owner owner = new Owner();
        owner.setUnreadMessages(unreadMessages);

        when(ownerService.findByLogin(anyString())).thenReturn(owner);

        mockMvc.perform(MockMvcRequestBuilders.get("/cabinet/get-unread-messages"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[1, 2]"));
    }

    @Test
    @WithMockUser(username = "user@example.com", password = "12345678")
    public void testDeleteSelected() throws Exception {
        owner.setUnreadMessages(messagesList);
        when(ownerService.findByLogin(anyString())).thenReturn(owner);
        when(ownerService.findOwnerDTObyEmail(anyString())).thenReturn(ownerDTO);
        when(ownerService.findOwnerDTObyEmailWithMessages(anyString())).thenReturn(ownerDTO);


        when(messageService.findById(1L)).thenReturn(message1);
        when(messageService.findById(2L)).thenReturn(message2);

        mockMvc.perform(get("/cabinet/message/deleteSelected")
                        .param("checkboxList", "1", "2")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        // Проверка изменений
        mockMvc.perform(get("/cabinet/messages"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("messages"))
                .andExpect(model().attributeDoesNotExist("message"));
    }




    @Test
    @WithMockUser(username = "user@example.com", password = "12345678")
    public void getMessages_shouldReturnPageOfMessages() throws Exception {

        when(ownerService.findByLogin(anyString())).thenReturn(owner);
        Page<Message> messages = new PageImpl<>(List.of(new Message()));

        when(messageService.findAllBySpecification(form, 0, 10, owner.getId())).thenReturn(messages);

        ObjectMapper mapper = new ObjectMapper();
        String filters = mapper.writeValueAsString(form);


        mockMvc.perform(MockMvcRequestBuilders.get("/cabinet/get-messages")
                        .param("page", "0")
                        .param("size", "10")
                        .param("filters", filters))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json("{}"));
//            .andExpect(MockMvcResultMatchers.content().json("{\"content\":[{}],\"pageable\":{\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"offset\":0,\"pageSize\":10,\"pageNumber\":0,\"unpaged\":false,\"paged\":true},\"totalElements\":1,\"last\":true,\"totalPages\":1,\"size\":10,\"number\":0,\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"numberOfElements\":1,\"first\":true,\"empty\":false}"));
    }


//          requests


    @Test
    @WithMockUser(username = "user@example.com", password = "12345678")
    public void testGetRequestPage() throws Exception {

        when(ownerService.findOwnerDTObyEmail(anyString())).thenReturn(ownerDTO);

        mockMvc.perform(get("/cabinet/requests"))
                .andExpect(status().isOk())
                .andExpect(view().name("cabinet/requests"))
                .andExpect(model().attributeExists("owner"))
                .andExpect(model().attributeExists("filterForm"))
                .andExpect(model().attribute("requestsPageActive", true));
    }

    @Test
    @WithMockUser(username = "user@example.com", password = "12345678")
    public void testDeleteRequest() throws Exception {
        long requestId = 1L;

        mockMvc.perform(get("/cabinet/request/delete/{id}", requestId))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cabinet/requests"));

        verify(repairRequestRepository, times(1)).deleteById(requestId);
    }

    @Test
    @WithMockUser(username = "user@example.com", password = "12345678")
    public void testGetCreateRequestPage() throws Exception {
        when(ownerService.findOwnerDTObyEmail(anyString())).thenReturn(ownerDTO);
        List<UserRole> mockMasterRoles = new ArrayList<>();
        when(userRoleRepository.findAllMasterRoles()).thenReturn(mockMasterRoles);

        mockMvc.perform(get("/cabinet/requests/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("cabinet/request_card"))
                .andExpect(model().attributeExists("owner"))
                .andExpect(model().attributeExists("repairRequest"))
                .andExpect(model().attributeExists("masters"))
                .andExpect(model().attribute("requestsPageActive", true));
    }


    @Test
    @WithMockUser(username = "user@example.com", password = "12345678")
    public void testSaveRequestWithValidData() throws Exception {
        RepairRequest repairRequest = new RepairRequest();

        when(ownerService.findOwnerDTObyEmail(anyString())).thenReturn(ownerDTO);
        when(ownerService.findByLogin(anyString())).thenReturn(owner);
        when(repairRequestRepository.save(any(RepairRequest.class))).thenReturn(repairRequest);
        when(userRoleRepository.findById(anyLong())).thenReturn(Optional.of(new UserRole()));

        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(apartmentService.findById(anyLong())).thenReturn(apartment);


        //   какая то дрочь с передаваемыми полями master и apartment
        // передаешь лонг требует объект и наоборот


        mockMvc.perform(post("/cabinet/request/save")
//                        .flashAttr("repairRequest", repairRequest)
                        .param("id", "1")
                        .flashAttr("apartment", 1)
                        .flashAttr("master", 1)
                        .param("description", "Test description")
                        .param("date", "2023-07-13")
                        .param("time", "12:00")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cabinet/requests?apartment=1&master=1"));

        verify(repairRequestRepository).save(any(RepairRequest.class));
    }

    @Test
    @WithMockUser(username = "user@example.com", password = "12345678")
    public void testGetRequests() throws Exception {
        Principal principal = mock(Principal.class);

        when(principal.getName()).thenReturn("user@example.com");

        when(ownerService.findByLogin(anyString())).thenReturn(owner);

        when(ownerDTOMapper.toDTOcabinetProfile(any(Owner.class))).thenReturn(ownerDTO);

        when(ownerService.findByLogin(anyString())).thenReturn(owner);

        Page<RepairRequestDTO> repairRequestPage = new PageImpl<>(Collections.emptyList());
        when(repairRequestService.findReqoestDtoByOwnerId(anyLong(), any(Pageable.class)))
                .thenReturn(repairRequestPage);


        mockMvc.perform(get("/cabinet/get-requests")
                        .param("page", "1")
                        .param("size", "10")
                        .param("filters", "your_filters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(0)));
    }


//      owner


    @Test
    @WithMockUser(username = "user@example.com", password = "12345678")
    public void testGetUserProfilePage() throws Exception {
        when(ownerService.findByLogin(anyString())).thenReturn(owner);


        when(ownerDTOMapper.toDTOcabinetProfile(any(Owner.class))).thenReturn(ownerDTO);

        mockMvc.perform(get("/cabinet/user/view"))
                .andExpect(status().isOk())
                .andExpect(view().name("cabinet/user_profile"))
                .andExpect(model().attributeExists("owner"))
                .andExpect(model().attribute("owner", ownerDTO))
                .andExpect(model().attributeExists("profilePageActive"))
                .andExpect(model().attribute("profilePageActive", true));
    }

    @Test
    @WithMockUser(username = "user@example.com", password = "12345678")
    public void testGetEditUserProfilePage() throws Exception {

        when(ownerService.findByLogin(anyString())).thenReturn(owner);

        when(ownerDTOMapper.toDTOcabinetEditProfile(any(Owner.class))).thenReturn(ownerDTO);


        mockMvc.perform(get("/cabinet/user/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("cabinet/user_edit"))
                .andExpect(model().attributeExists("owner"))
                .andExpect(model().attribute("owner", ownerDTO))
                .andExpect(model().attributeExists("profilePageActive"))
                .andExpect(model().attribute("profilePageActive", true));
    }

    @Test
    @WithMockUser(username = "user@example.com", password = "12345678")
    public void testSaveOwner() throws Exception {
        OwnerDTO ownerDTO = new OwnerDTO();
        ownerDTO.setId(1L); // Установите значение id, если необходимо
        ownerDTO.setOldpassword("12345678"); // Устанавливаем oldpassword в объекте OwnerDTO

        Owner oldOwner = new Owner(); // Создаем экземпляр класса Owner
        String encodedPassword = new BCryptPasswordEncoder().encode("12345678"); // Закодируйте пароль
        oldOwner.setPassword(encodedPassword); // Установите закодированный пароль в oldOwner

        doReturn(owner).when(ownerDTOMapper).toEntityСabinetEditProfile(any(OwnerDTO.class));
        doReturn(oldOwner).when(ownerService).findById(anyLong());

        String savedImageName = "saved_image.jpg";
        when(ownerService.saveOwnerImage(anyLong(), any(MultipartFile.class))).thenReturn(savedImageName);

        MultipartFile imageFile = new MockMultipartFile("img1", new byte[0]);

        mockMvc.perform(multipart("/cabinet/user/save")
                        .file((MockMultipartFile) imageFile)
                        .param("newPassword", "new_password")
                        .param("repassword", "new_password")
                        .param("oldpassword", "12345678")
                        .param("id", "1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cabinet/user/view"));

        verify(ownerService).findById(1L);
        verify(ownerService).saveOwnerImage(1L, imageFile);
        verify(ownerService).save(any(Owner.class));
    }


}