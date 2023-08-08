package com.example.myhome.services;

import com.example.myhome.home.dto.OwnerDTO;
import com.example.myhome.home.mapper.OwnerDTOMapper;
import com.example.myhome.home.model.*;
import com.example.myhome.home.repository.OwnerRepository;
import com.example.myhome.home.service.OwnerService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ComponentScan("com.example.myhome.home.service.impl")
public class OwnerServiceTest {

    @Autowired
    private OwnerService ownerService;

    @MockBean
    private OwnerRepository ownerRepository;


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
        testOwner.setUnreadMessages(new ArrayList<>());

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

        testOwner.setApartments(List.of(apartment, apartment, apartment));

        mapper = new OwnerDTOMapper();

        testDTO = mapper.fromOwnerToDTO(testOwner);

        ownerList = List.of(testOwner, testOwner, testOwner);
        ownerDTOList = List.of(testDTO, testDTO, testDTO);

        ownerPage = new PageImpl<>(ownerList, PageRequest.of(1, 1), 1);
        ownerDTOPage = new PageImpl<>(ownerDTOList, PageRequest.of(1, 1), 1);
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
        when(ownerRepository.findByNameFragment(anyString(), any(Pageable.class))).thenReturn(ownerPage);
        when(ownerRepository.getReferenceById(anyLong())).thenReturn(testOwner);
        when(ownerRepository.save(any(Owner.class))).thenReturn(testOwner);
    }


    @Test
    void findByIdTest() {
        assertThat(ownerService.findById(testOwner.getId())).isEqualTo(testOwner);
    }


    @Test
    void findByLoginTest() {
        assertThat(ownerService.findByLogin(testOwner.getEmail())).isEqualTo(testOwner);
    }


    @Test
    public void testIsOwnerExistsByEmail_ExistingOwner() {
        String email = "test@example.com";
        Owner owner = new Owner();
        owner.setEmail(email);
        when(ownerRepository.findByEmail(email)).thenReturn(Optional.of(owner));

        // Act
        boolean result = ownerService.isOwnerExistsByEmail(email);

        // Assert
        assertTrue(result);
    }



    @Test
    void saveTest() {

        assertThat(ownerService.save(testOwner)).isEqualTo(testOwner);
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
    void findOwnerDTOByEmailTest() {
        assertThat(ownerService.findOwnerDTObyEmail(testOwner.getEmail())).isInstanceOf(OwnerDTO.class);
    }

    @Test
    void findOwnerDTOByEmailWithMessagesTest() {
        assertThat(ownerService.findOwnerDTObyEmailWithMessages(testOwner.getEmail())).isInstanceOf(OwnerDTO.class);
    }

}
