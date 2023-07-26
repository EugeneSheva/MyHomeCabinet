package com.example.myhome.services;

import com.example.myhome.home.dto.RepairRequestDTO;
import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.mapper.RepairRequestDTOMapper;
import com.example.myhome.home.model.*;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.*;
import com.example.myhome.home.service.AdminService;
import com.example.myhome.home.service.ApartmentService;
import com.example.myhome.home.service.OwnerService;
import com.example.myhome.home.service.RepairRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RequestServiceTest {

    @Autowired
    RepairRequestService repairRequestService;

    @MockBean
    RepairRequestRepository repairRequestRepository;

    @MockBean private ApartmentService apartmentService;
    @MockBean private OwnerService ownerService;
    @MockBean private AdminService adminService;

    @MockBean private ApartmentRepository apartmentRepository;
    @MockBean private OwnerRepository ownerRepository;
    @MockBean private AdminRepository adminRepository;
    @MockBean private UserRoleRepository userRoleRepository;

    RepairRequest request = new RepairRequest();
    RepairRequestDTOMapper mapper = new RepairRequestDTOMapper();

    @BeforeEach
    void setupRequest() {
        request.setId(1L);
        request.setBest_time_request(LocalDateTime.now());
        request.setOwner(new Owner());
        request.setApartment(new Apartment());
        request.setMaster(new Admin());
        UserRole role = new UserRole();
        role.setName("test");
        request.setMaster_type(role);
        request.setDescription("test");
        request.setRequest_date(LocalDateTime.now());
        request.setStatus(RepairStatus.ACCEPTED);
        request.setComment("test");
    }











}
