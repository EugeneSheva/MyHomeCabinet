package com.example.myhome.services;

import com.example.myhome.home.model.Admin;
import com.example.myhome.home.service.impl.UserRoleServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.example.myhome.home.model.PageRoleDisplay;
import com.example.myhome.home.model.UserRole;
import com.example.myhome.home.repository.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.example.myhome.home.model.PageRoleDisplay;
import com.example.myhome.home.model.UserRole;
import com.example.myhome.home.repository.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class UserRoleServiceImplTest {
    @Mock
    private UserRoleRepository userRepository;

    @InjectMocks
    private UserRoleServiceImpl userRoleService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up the authentication context with an Admin object
        Admin admin = new Admin();
        admin.setRole(new UserRole());
        admin.setPassword("admin_password");
        Authentication auth = new UsernamePasswordAuthenticationToken(admin, admin.getPassword(), Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void getRole_ValidId_ReturnsUserRole() {
        Long roleId = 1L;
        UserRole userRole = new UserRole();
        userRole.setId(roleId);
        when(userRepository.findById(roleId)).thenReturn(java.util.Optional.of(userRole));

        UserRole result = userRoleService.getRole(roleId);

        assertNotNull(result);
        assertEquals(roleId, result.getId());
        verify(userRepository, times(1)).findById(roleId);
    }

    @Test
    void getRole_InvalidId_ReturnsNull() {
        Long roleId = 100L;
        when(userRepository.findById(roleId)).thenReturn(java.util.Optional.empty());

        UserRole result = userRoleService.getRole(roleId);

        assertNull(result);
        verify(userRepository, times(1)).findById(roleId);
    }

    @Test
    void saveRole_ValidRole_ReturnsSavedRole() {
        UserRole userRole = new UserRole();
        when(userRepository.save(userRole)).thenReturn(userRole);

        UserRole result = userRoleService.saveRole(userRole);

        assertNotNull(result);
        verify(userRepository, times(1)).save(userRole);
    }

    @Test
    void deleteRole_ValidId_DeletesRole() {
        Long roleId = 1L;
        doNothing().when(userRepository).deleteById(roleId);

        userRoleService.deleteRole(roleId);

        verify(userRepository, times(1)).deleteById(roleId);
    }

    @Test
    void updateRoles_ValidPageRoleDisplayList_PermissionsUpdated() {

        UserRole accountant = new UserRole();
        UserRole admin = new UserRole();
        UserRole director = new UserRole();
        UserRole electrician = new UserRole();
        UserRole manager = new UserRole();
        UserRole plumber = new UserRole();

        PageRoleDisplay pageRole1 = new PageRoleDisplay();
        pageRole1.setCode("code1");
        pageRole1.setRole_accountant(true);
        pageRole1.setRole_admin(true);

        PageRoleDisplay pageRole2 = new PageRoleDisplay();
        pageRole2.setCode("code2");
        pageRole2.setRole_admin(true);
        pageRole2.setRole_director(true);

        PageRoleDisplay pageRole3 = new PageRoleDisplay();
        pageRole3.setCode("code3");
        pageRole3.setRole_electrician(true);
        pageRole3.setRole_manager(true);


        List<PageRoleDisplay> pageRoleList = Arrays.asList(pageRole1, pageRole2, pageRole3);

        when(userRepository.findByName("Бухгалтер")).thenReturn(java.util.Optional.of(accountant));
        when(userRepository.findByName("Администратор")).thenReturn(java.util.Optional.of(admin));
        when(userRepository.findByName("Директор")).thenReturn(java.util.Optional.of(director));
        when(userRepository.findByName("Электрик")).thenReturn(java.util.Optional.of(electrician));
        when(userRepository.findByName("Управляющий")).thenReturn(java.util.Optional.of(manager));
        when(userRepository.findByName("Сантехник")).thenReturn(java.util.Optional.of(plumber));

        userRoleService.updateRoles(pageRoleList);
        assertTrue(accountant.getPermissions().containsAll(Arrays.asList("code1.read", "code1.write")));
        assertTrue(admin.getPermissions().containsAll(Arrays.asList("code1.read", "code2.write")));
        assertTrue(director.getPermissions().containsAll(Arrays.asList("code2.read", "code2.write")));
        assertTrue(electrician.getPermissions().containsAll(Arrays.asList("code3.read")));
        assertTrue(manager.getPermissions().containsAll(Arrays.asList("code3.read", "code3.write")));
        assertTrue(plumber.getPermissions().isEmpty());
    }

    @Test
    void clearRoles_PermissionsClearedForAllRoles() {
        UserRole userRole1 = new UserRole();
        userRole1.getPermissions().add("permission1");
        UserRole userRole2 = new UserRole();
        userRole2.getPermissions().add("permission2");
        when(userRepository.findAll()).thenReturn(Arrays.asList(userRole1, userRole2));

        userRoleService.clearRoles();

        assertTrue(userRole1.getPermissions().isEmpty());
        assertTrue(userRole2.getPermissions().isEmpty());
    }

    @Test
    void resetPrincipal_PrincipalResetWithCorrectAuthorities() {
        UserRole userRole = new UserRole();
        userRole.setName("RoleName");
        userRole.getPermissions().add("permission1");
        userRole.getPermissions().add("permission2");
        Admin admin = new Admin();
        admin.setRole(userRole);
        admin.setPassword("password");

        Authentication existingAuth = new UsernamePasswordAuthenticationToken(admin, admin.getPassword(), Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(existingAuth);

        when(userRepository.findByName("RoleName")).thenReturn(java.util.Optional.of(userRole));

        userRoleService.resetPrincipal();

        Authentication updatedAuth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(updatedAuth);
        assertTrue(updatedAuth.isAuthenticated());
        assertTrue(updatedAuth.getAuthorities().contains(new SimpleGrantedAuthority("permission1")));
        assertTrue(updatedAuth.getAuthorities().contains(new SimpleGrantedAuthority("permission2")));
    }
}