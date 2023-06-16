package com.example.myhome.home.service.impl;

import com.example.myhome.home.model.Admin;
import com.example.myhome.home.model.PageRoleDisplay;
import com.example.myhome.home.model.UserRole;
import com.example.myhome.home.repository.UserRoleRepository;
import com.example.myhome.home.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository repository;


    public UserRole getRole(Long role_id) {
        return repository.findById(role_id).orElse(null);
    }

    public UserRole saveRole(UserRole role) {
        return repository.save(role);
    }

    public void deleteRole(Long role_id) {
        repository.deleteById(role_id);
    }

    @Override
    @Transactional
    public void updateRoles(List<PageRoleDisplay> list) {

        clearRoles();

        UserRole accountant = repository.findByName("Бухгалтер").orElseThrow();
        UserRole admin = repository.findByName("Администратор").orElseThrow();
        UserRole director = repository.findByName("Директор").orElseThrow();
        UserRole electrician = repository.findByName("Электрик").orElseThrow();
        UserRole manager = repository.findByName("Управляющий").orElseThrow();
        UserRole plumber = repository.findByName("Сантехник").orElseThrow();

        for(PageRoleDisplay pageRole : list) {
            if(pageRole.getRole_accountant()) {
                accountant.getPermissions().add(pageRole.getCode()+".read");
                accountant.getPermissions().add(pageRole.getCode()+".write");
            }
            if(pageRole.getRole_admin()) {
                admin.getPermissions().add(pageRole.getCode()+".read");
                admin.getPermissions().add(pageRole.getCode()+".write");
            }
            if(pageRole.getRole_director()) {
                director.getPermissions().add(pageRole.getCode()+".read");
                director.getPermissions().add(pageRole.getCode()+".write");
            }
            if(pageRole.getRole_electrician()) {
                electrician.getPermissions().add(pageRole.getCode()+".read");
                electrician.getPermissions().add(pageRole.getCode()+".write");
            }
            if(pageRole.getRole_manager()) {
                manager.getPermissions().add(pageRole.getCode()+".read");
                manager.getPermissions().add(pageRole.getCode()+".write");
            }
            if(pageRole.getRole_plumber()) {
                plumber.getPermissions().add(pageRole.getCode()+".read");
                plumber.getPermissions().add(pageRole.getCode()+".write");
            }
        }

        resetPrincipal();
    }

    @Transactional
    public void clearRoles() {
        for(UserRole role : repository.findAll()) role.getPermissions().clear();
    }

    public void resetPrincipal() {
        Admin admin = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(null);

        List<GrantedAuthority> authorities = new ArrayList<>();
        repository.findByName(admin.getRole().getName())
            .ifPresent(adminRole ->
                        adminRole.getPermissions()
                                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission))));

        Authentication auth = new UsernamePasswordAuthenticationToken(admin, admin.getPassword(), authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

}
