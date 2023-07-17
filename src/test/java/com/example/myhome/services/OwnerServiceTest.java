package com.example.myhome.services;

import com.example.myhome.home.dto.OwnerDTO;
import com.example.myhome.home.mapper.OwnerDTOMapper;
import com.example.myhome.home.model.*;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.OwnerRepository;
import com.example.myhome.home.service.ApartmentService;
import com.example.myhome.home.service.BuildingService;
import com.example.myhome.home.service.OwnerService;
import com.example.myhome.util.FileUploadUtil;
import com.example.myhome.util.UserStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@ComponentScan("com.example.myhome.home.service.impl")
public class OwnerServiceTest {

    @Autowired
    private OwnerService ownerService;

    @MockBean private OwnerRepository ownerRepository;
    @MockBean private BuildingService buildingService;
    @MockBean private ApartmentService apartmentService;
    @MockBean private FileUploadUtil fileUploadUtil;

    static Owner testOwner;
    static OwnerDTO testDTO;

    static List<Owner> ownerList;
    static Page<Owner> ownerPage;

    static List<OwnerDTO> ownerDTOList;
    static Page<OwnerDTO> ownerDTOPage;

    static OwnerDTOMapper mapper;

    @BeforeAll
    static void setupObjects() {
        testOwner = new Owner();
        testOwner.setId(1L);
        testOwner.setStatus(UserStatus.NEW);
        testOwner.setBirthdate(LocalDate.now());
        testOwner.setViber("test");
        testOwner.setEnabled(true);
        testOwner.setEmail("test");
        testOwner.setPassword("test");
        testOwner.setHas_debt(true);
        testOwner.setAdded_at(LocalDateTime.now());
        testOwner.setDescription("test");
        testOwner.setFirst_name("test");
        testOwner.setLast_name("test");
        testOwner.setFathers_name("test");
        testOwner.setProfile_picture("test");

        Apartment apartment = new Apartment();
        apartment.setId(1L);
        apartment.setOwner(testOwner);
        apartment.setTariff(new Tariff());
        apartment.setSquare(100.0);
        apartment.setBalance(100.0);
        apartment.setNumber(100L);
        ApartmentAccount account = new ApartmentAccount();
        account.setId(1L);
        apartment.setAccount(account);
        apartment.setBuilding(new Building());
        apartment.setFloor("1");
        apartment.setSection("1");

        testOwner.setApartments(List.of(apartment,apartment,apartment));

        mapper = new OwnerDTOMapper();

        testDTO = mapper.fromOwnerToDTO(testOwner);

        ownerList = List.of(testOwner, testOwner, testOwner);
        ownerDTOList = List.of(testDTO, testDTO, testDTO);

        ownerPage = new PageImpl<>(ownerList, PageRequest.of(1,1), 1);
        ownerDTOPage = new PageImpl<>(ownerDTOList, PageRequest.of(1,1),1);
    }

    @BeforeEach
    void setupMocks() {
        when(ownerRepository.findById(anyLong())).thenReturn(Optional.of(testOwner));
        when(ownerRepository.findByEmail(anyString())).thenReturn(Optional.of(testOwner));
        when(ownerRepository.findAll()).thenReturn(ownerList);
        when(ownerRepository.findAll(any(Pageable.class))).thenReturn(ownerPage);
        when(ownerRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(ownerPage);
//        when(ownerRepository.findByFilters(anyLong(), anyString(), anyString(),anyString(),anyString(),
//                anyLong(), any(LocalDate.class),any(UserStatus.class),anyBoolean(), any(Pageable.class))).thenReturn(ownerPage);
        when(ownerRepository.findByName(anyString(), any(Pageable.class))).thenReturn(ownerPage);
        when(ownerRepository.findByNameFragment(anyString(),any(Pageable.class))).thenReturn(ownerPage);
        when(ownerRepository.getReferenceById(anyLong())).thenReturn(testOwner);
    }

    @Test
    void contextLoads() {

    }

    @Test
    void findByIdTest() {
        assertThat(ownerService.findById(testOwner.getId())).isEqualTo(testOwner);
    }

    @Test
    void findByIdDTOTest() {
        assertThat(ownerService.findByIdDTO(testOwner.getId())).isEqualTo(testDTO);
    }

    @Test
    void findByLoginTest() {
        assertThat(ownerService.findByLogin(testOwner.getEmail())).isEqualTo(testOwner);
    }

    @Test
    void findAllTest() {
        assertThat(ownerService.findAll()).isEqualTo(ownerList);
    }

    @Test
    void findAllPageTest() {
        assertThat(ownerService.findAll(PageRequest.of(1,1))).isEqualTo(ownerPage);
    }

    @Test
    void findOwnerApartmentsTest() {
        assertThat(ownerService.findOwnerApartments(testOwner.getId())).isInstanceOf(List.class);
        assertThat(ownerService.findOwnerApartments(testOwner.getId())).hasSize(3);
    }

    @Test
    void findAllDTOTest() {
        assertThat(ownerService.findAllDTO()).isEqualTo(ownerDTOList);
    }

    @Test
    void getOwnerDTOByPage() {
        assertThat(ownerService.getOwnerDTOByPage(testOwner.getFullName(), 1)).isEqualTo(ownerDTOList);
    }

    @Test
    void findAllDTOPageTest() {
        assertThat(ownerService.findAllDTO(PageRequest.of(1,1))).isEqualTo(ownerDTOPage);
    }

    @Test
    void findAllBySpecificationTest() {
        FilterForm filters = new FilterForm();
        filters.setId(1L);
        filters.setOwnerName("test");
        filters.setPhone("test");
        filters.setEmail("test");
        filters.setBuilding(1L);
        filters.setApartment(1L);
        filters.setDate("2022-11-11");
        filters.setStatus("NEW");
        filters.setDebt(true);

        assertThat(ownerService.findAllBySpecification(filters,1,1)).isInstanceOf(Page.class);
        assertThat(ownerService.findAllBySpecification(filters,1,1).getContent()).hasSize(3);
        assertThat(ownerService.findAllBySpecification(filters,1,1).getContent()).hasAtLeastOneElementOfType(OwnerDTO.class);
    }

    @Test
    void buildSpecTest() {
        FilterForm filters = new FilterForm();
        filters.setId(1L);
        filters.setOwnerName("test");
        filters.setPhone("test");
        filters.setEmail("test");
        filters.setBuilding(1L);
        filters.setApartment(1L);
        filters.setDate("2022-11-11");
        filters.setStatus("NEW");
        filters.setDebt(true);

        assertThat(ownerService.buildSpecFromFilters(filters)).isInstanceOf(Specification.class);
    }

    @Test
    void findAllBySpecificationTest_2() {
        FilterForm filters = new FilterForm();
        filters.setId(1L);
        filters.setOwnerName("test");
        filters.setPhone("test");
        filters.setEmail("test");
        filters.setBuilding(1L);
        filters.setApartment(1L);
        filters.setDate("2022-11-11");
        filters.setStatus("NEW");
        filters.setDebt(true);

//        when(ownerRepository.findByFilters(anyLong(), anyString(), anyString(),anyString(),anyString(),
//                anyLong(), any(LocalDate.class),any(UserStatus.class),anyBoolean(), any(Pageable.class))).thenReturn(ownerPage);

        assertThat(ownerService.findAllBySpecification2(filters,1,1)).isInstanceOf(Page.class);
        assertThat(ownerService.findAllBySpecification2(filters,1,1).getContent()).hasSize(3);
        assertThat(ownerService.findAllBySpecification2(filters,1,1).getContent()).hasAtLeastOneElementOfType(OwnerDTO.class);
    }

    @Test
    void findByNameFragmentDTOTest() {
        assertThat(ownerService.findByNameFragmentDTO(testOwner.getFullName(), PageRequest.of(1,1))).isInstanceOf(Page.class);
        assertThat(ownerService.findByNameFragmentDTO(testOwner.getFullName(), PageRequest.of(1,1)).getContent()).hasSize(1);
    }

    @Test
    void saveTest() {
        assertThat(ownerService.save(testOwner)).isEqualTo(testOwner);
    }

//    @Test
//    void saveDTOTest() {
//        assertThat(ownerService.save(testDTO)).isEqualTo(testOwner);
//    }

    @Test
    void deleteByIdTest() {
        ownerService.deleteById(testOwner.getId());
    }

    @Test
    void getQuantityTest() {
        assertThat(ownerService.getQuantity()).isEqualTo(3L);
    }

    @Test
    void getOwnerApartmentAccountIdsTest() {
        assertThat(ownerService.getOwnerApartmentAccountsIds(testOwner.getId())).isInstanceOf(List.class);
        assertThat(ownerService.getOwnerApartmentAccountsIds(testOwner.getId())).hasSameElementsAs(List.of(1L,1L,1L));
    }

    @Test
    void saveOwnerImagesTest() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "multipart/form-data", new byte[1]);
        assertThat(ownerService.saveOwnerImage(testOwner.getId(), file)).isInstanceOf(String.class);
    }

    @Test
    void saveOwnerImagesTest_2() throws Exception {
        MockMultipartFile empty_file = new MockMultipartFile("empty_file", "empty_file.txt", "multipart/form-data", new byte[0]);
        assertThat(ownerService.saveOwnerImage(testOwner.getId(), empty_file)).isInstanceOf(String.class);
    }

    @Test
    void hasDebtTest() {
        assertThat(ownerService.isHaveDebt("haveDebt")).isTrue();
        assertThat(ownerService.isHaveDebt("noDebt")).isFalse();
    }

    @Test
    void stringStatusConverterTest() {
        assertThat(ownerService.stringStatusConverter("active")).isEqualTo(UserStatus.ACTIVE);
        assertThat(ownerService.stringStatusConverter("new")).isEqualTo(UserStatus.NEW);
        assertThat(ownerService.stringStatusConverter("disabled")).isEqualTo(UserStatus.DISABLED);
    }

    @Test
    void countAllOwners() {
        assertThat(ownerService.countAllOwners()).isEqualTo(3L);
    }

    @Test
    void findOwnerDTOByEmailTest() {
        assertThat(ownerService.findOwnerDTObyEmail(testOwner.getEmail())).isInstanceOf(OwnerDTO.class);
    }

    @Test
    void findOwnerDTOByEmailWithMessagesTest() {
        assertThat(ownerService.findOwnerDTObyEmailWithMessages(testOwner.getEmail())).isInstanceOf(OwnerDTO.class);
    }

    @Test
    void findOwnerDTOByEmailFullTest() {
        assertThat(ownerService.findOwnerDTObyEmailFull(testOwner.getEmail())).isInstanceOf(OwnerDTO.class);
    }

    @Test
    void convertOwnerToOwnerDTOTest() {
        assertThat(ownerService.convertOwnerToOwnerDTO(testOwner)).isInstanceOf(OwnerDTO.class);
    }





}
