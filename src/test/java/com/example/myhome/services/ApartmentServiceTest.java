package com.example.myhome.services;

import com.example.myhome.home.dto.ApartmentDTO;
import com.example.myhome.home.dto.OwnerDTO;
import com.example.myhome.home.mapper.ApartmentDTOMapper;
import com.example.myhome.home.model.*;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.ApartmentRepository;
import com.example.myhome.home.service.ApartmentService;
import com.example.myhome.util.FileUploadUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@ComponentScan("com.example.myhome.home.service.impl")
public class ApartmentServiceTest {

    @Autowired
    private ApartmentService apartmentService;

    @MockBean
    private ApartmentRepository apartmentRepository;

    @MockBean
    private FileUploadUtil fileUploadUtil;

    static Apartment testApartment;
    static ApartmentDTO testDTO;
    static ApartmentDTOMapper apartmentMapper;
    static ObjectMapper jsonMapper;

    static List<Apartment> apartmentList;
    static Page<Apartment> apartmentPage;
    static List<ApartmentDTO> apartmentDTOList;
    static Page<ApartmentDTO> apartmentDTOPage;

    @BeforeAll
    static void setupObjects() {
        testApartment = new Apartment();
        testApartment.setId(1L);
        testApartment.setNumber(1L);
        testApartment.setAccount(new ApartmentAccount());
        testApartment.setBuilding(new Building());
        testApartment.setOwner(new Owner());
        testApartment.setBalance(100.0);
        testApartment.setTariff(new Tariff());
        testApartment.setSquare(100.0);

        apartmentMapper = new ApartmentDTOMapper();

        testDTO = apartmentMapper.fromApartmentToDTO(testApartment);
        OwnerDTO ownerDTO = new OwnerDTO();
        ownerDTO.setText(null);
        ownerDTO.setFirst_name("");
        ownerDTO.setFathers_name("");
        ownerDTO.setLast_name("");
        ownerDTO.setFullName("");

        ownerDTO.setPhone_number("");
        ownerDTO.setEmail("");
        ownerDTO.setPhone_number("");
        ownerDTO.setBuildings(new ArrayList<>());
        ownerDTO.setApartments(new ArrayList<>());
        ownerDTO.setApartmentNames("");
        ownerDTO.setDate("");
        ownerDTO.setStatus("");
        ownerDTO.setUnreadMessageQuantity(0L);

        testDTO.setOwner(new OwnerDTO());

        jsonMapper = new JsonMapper();

        apartmentList = List.of(testApartment, testApartment, testApartment);
        apartmentDTOList = List.of(testDTO, testDTO, testDTO);
        apartmentPage = new PageImpl<>(apartmentList, PageRequest.of(1,1),1);
        apartmentDTOPage = new PageImpl<>(apartmentDTOList, PageRequest.of(1,1),1);
    }

    @BeforeEach
    void setupMocks() {
        when(apartmentRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testApartment));
        when(apartmentRepository.save(any(Apartment.class))).thenReturn(testApartment);
        when(apartmentRepository.findAll()).thenReturn(apartmentList);
        when(apartmentRepository.findApartmentsByBalanceBefore(anyDouble())).thenReturn(apartmentList);
        when(apartmentRepository.findApartmentsByBuildingId(anyLong())).thenReturn(apartmentList);
        when(apartmentRepository.findApartmentsByBuildingIdAndBalanceBefore(anyLong(), anyDouble())).thenReturn(apartmentList);
        when(apartmentRepository.findApartmentsByBuildingIdAndSectionContainingIgnoreCase(anyLong(),anyString())).thenReturn(apartmentList);
        when(apartmentRepository.findApartmentsByBuildingIdAndSectionContainingIgnoreCaseAndBalanceBefore(anyLong(), anyString(), anyDouble())).thenReturn(apartmentList);
        when(apartmentRepository.findApartmentsByBuildingIdAndFloorContainingIgnoreCase(anyLong(),anyString())).thenReturn(apartmentList);
        when(apartmentRepository.findApartmentsByBuildingIdAndFloorContainingIgnoreCaseAndBalanceBefore(anyLong(),anyString(),anyDouble())).thenReturn(apartmentList);
        when(apartmentRepository.findApartmentsByBuildingIdAndSectionContainingIgnoreCaseAndFloorContainingIgnoreCase(anyLong(),anyString(),anyString())).thenReturn(apartmentList);
        when(apartmentRepository.findApartmentsByBuildingIdAndSectionContainingIgnoreCaseAndFloorContainingIgnoreCaseAndBalanceBefore(anyLong(),anyString(),anyString(),anyDouble())).thenReturn(apartmentList);
        when(apartmentRepository.findByFilters(anyLong(), anyString(), anyString(), anyString(), anyLong(), anyString(),
                any(Pageable.class))).thenReturn(apartmentPage);
        when(apartmentRepository.findByNumber(anyLong())).thenReturn(Optional.ofNullable(testApartment));
    }

    @Test
    void contextLoads() {
        assertThat(apartmentService).isNotNull();
    }

    @Test
    void findByIDTest() {
        assertThat(apartmentService.findById(testApartment.getId())).isEqualTo(testApartment);
    }

    @Test
    void findByNumberTest() {
        assertThat(apartmentService.findByNumber(testApartment.getNumber())).isEqualTo(testApartment);
    }



    @Test
    void convertApartmentsToApartmentsDTOTest() {
        assertThat(apartmentService.convertApartmentsToApartmentsDTO(apartmentList)).size().isEqualTo(apartmentDTOList.size());
    }



    @Test
    void findSingleDTOTest() {
        assertThat(apartmentService.findApartmentDto(testApartment.getId()).getId()).isEqualTo(testDTO.getId());
    }

}
