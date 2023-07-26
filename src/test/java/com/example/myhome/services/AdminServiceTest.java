package com.example.myhome.services;

import com.example.myhome.home.dto.AdminDTO;
import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.mapper.AdminDTOMapper;
import com.example.myhome.home.model.Admin;
import com.example.myhome.home.model.UserRole;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.AdminRepository;
import com.example.myhome.home.repository.UserRoleRepository;
import com.example.myhome.home.service.AdminService;
import com.example.myhome.home.specification.AdminSpecifications;
import com.example.myhome.home.validator.AdminValidator;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
class AdminServiceTest {

    @MockBean
    AdminRepository repository;

    @MockBean
    UserRoleRepository userRoleRepository;

    @Autowired
    AdminService service;

    @Autowired
    AdminValidator validator;

    @Autowired
    AdminDTOMapper mapper;

    Admin testAdmin;

    @BeforeEach
    void createAdmin() {
        testAdmin = new Admin();
        testAdmin.setId(1L);
        testAdmin.setPassword("test");
        testAdmin.setActive(true);
        testAdmin.setEmail("test");
        testAdmin.setPhone_number("test");
        testAdmin.setFirst_name("test");
        testAdmin.setLast_name("test");
        UserRole role = new UserRole();
        role.setId(1L);
        role.setName("test");
        role.setPermissions(new HashSet<>());
        testAdmin.setRole(role);
        testAdmin.setDateOfRegistry(LocalDate.now());
        testAdmin.setBuildings(new ArrayList<>());
        testAdmin.setCashBoxList(new ArrayList<>());
        testAdmin.setMessageList(new ArrayList<>());
        testAdmin.setCashBoxListManager(new ArrayList<>());
    }



    @Test
    void canFindAdminByIdTest() {
        Admin expected = new Admin();
        expected.setId(1L);
        expected.setFull_name("test test");

        given(repository.findById(1L)).willReturn(Optional.of(expected));

        Admin test = service.findAdminById(1L);

        verify(repository).findById(1L);

        assertThat(test).isEqualTo(expected);

        Admin test2 = service.findAdminById(2L);

        verify(repository).findById(2L);

        assertThat(test2).isNull();
    }

    @Test
    void canFindAdminByLoginTest() {
        Admin expected = new Admin();
        expected.setId(1L);
        expected.setFull_name("test test");
        expected.setEmail("test@gmail.com");

        given(repository.findByEmail("test@gmail.com")).willReturn(Optional.of(expected));

        Admin test = service.findAdminByLogin("test@gmail.com");

        verify(repository).findByEmail("test@gmail.com");

        assertThat(test).isEqualTo(expected);
    }

    @Test
    void canLoadAdminOptional() {
        Admin expected = new Admin();
        expected.setId(1L);
        expected.setFull_name("test test");

        given(repository.findById(1L)).willReturn(Optional.of(expected));

        Admin test = service.findAdminById(1L);

        verify(repository).findById(1L);

//        assertThat(service.findAdminById(1L)).isInstanceOf(Optional.class);
    }

    @Test
    void returnNullOnNoAdminFoundById() {
        assertThat(service.findAdminById(1L)).isNull();
    }

    @Test
    void returnNullOnNoAdminFoundByLogin() {assertThat(service.findAdminByLogin("test")).isNull();}



    @Test
    void transformIntoDTOTest() {
        UserRole role = new UserRole();
        role.setId(1L);
        role.setName("TestRole");
        Admin admin = new Admin();
        admin.setId(1L);
        admin.setFirst_name("test");
        admin.setLast_name("test");
        admin.setPhone_number("11111");
        admin.setEmail("test@gmail.com");
        admin.setPassword("testPassword");
        admin.setRole(role);

        AdminDTO dto = mapper.fromAdminToDTO(admin);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getFullName()).isEqualTo("test test");
        assertThat(dto.getPhone_number()).isEqualTo("11111");
        assertThat(dto.getEmail()).isEqualTo("test@gmail.com");
        assertThat(dto.getRole()).isEqualTo("TestRole");
        assertThat(dto.getUserRoleID()).isEqualTo(1L);
    }

    @Test
    void dtoDoesntShowPassword() {
        Admin admin = testAdmin;

        AdminDTO dto = mapper.fromAdminToDTO(admin);

        assertThat(dto.getPassword()).isNull();
    }

    @Test
    void transformDTOIntoAdminTest() {
        AdminDTO dto = new AdminDTO();
        dto.setId(1L);
        dto.setFirst_name("test");
        dto.setLast_name("test");
        dto.setEmail("test@gmail.com");
        dto.setRole("TestRole");
        dto.setUserRoleID(1L);
        dto.setPhone_number("11111");
        dto.setPassword("test");

        Admin admin = mapper.fromDTOToAdmin(dto);

        assertThat(admin.getId()).isEqualTo(1L);
        assertThat(admin.getFirst_name()).isEqualTo("test");
        assertThat(admin.getLast_name()).isEqualTo("test");
        assertThat(admin.getEmail()).isEqualTo("test@gmail.com");
        assertThat(admin.getPhone_number()).isEqualTo("11111");
    }

    @Test
    void canLoadUserByUsernameTest() {
        Admin admin = new Admin();
        admin.setId(1L);
        admin.setEmail("test");
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(admin));
        assertThat(service.loadUserByUsername(admin.getEmail())).isEqualTo(admin);
    }

}