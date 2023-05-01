package com.example.myhome.home.service.impl;

import com.example.myhome.home.model.Admin;
import com.example.myhome.home.dto.AdminDTO;

import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.AdminRepository;

import com.example.myhome.home.service.AdminService;
import com.example.myhome.home.specification.AdminSpecifications;
import com.example.myhome.util.MappingUtils;
import com.example.myhome.util.UserRole;
import lombok.RequiredArgsConstructor;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log
public class AdminServiceImpl implements AdminService {

    @Autowired private final AdminRepository adminRepository;
    @Autowired private final PasswordEncoder passwordEncoder;

    @Override
    public Admin findAdminById (Long id) {
        log.info("Searching for admin with ID: " + id);
        Optional<Admin> opt = adminRepository.findById(id);
        if(opt.isPresent()) {
            log.info("Found admin! ");
            log.info(opt.get().toString());
            return opt.get();
        } else {
            log.severe("Admin not found!");
            return null;
        }
    }

    @Override
    public Admin findAdminByLogin(String login) {
        log.info("Searching for admin with login: " + login);
        Optional<Admin> opt = adminRepository.findByEmail(login);
        if(opt.isPresent()) {
            log.info("Found admin!");
            log.info(opt.get().toString());
            return opt.get();
        } else {
            log.severe("Admin not found!");
            return null;
        }
    }

    @Override
    public List<Admin> findAll() {
        log.info("Getting all admins...");
        List<Admin> list = adminRepository.findAll();
        log.info("Found " + list.size() + " elements");
        return list;
    }

    @Override
    public Page<Admin> findAll(Pageable pageable) {
        log.info("Getting all admins...");
        Page<Admin> initialPage = adminRepository.findAll(pageable);
        log.info("Found " + initialPage.getContent().size() + " elements (page " + pageable.getPageNumber()+1 + "/" + initialPage.getTotalPages() + ")");
        return initialPage;
    }

    @Override
    public List<AdminDTO> findAllDTO(){
        log.info("Getting all admins...");
        List<Admin> initialList = adminRepository.findAll();
        log.info("Found " + initialList.size() + " elements");
        return initialList.stream().map(MappingUtils::fromAdminToDTO).collect(Collectors.toList());
    }

    @Override
    public Page<AdminDTO> findAllDTO(Pageable pageable) {
        log.info("Getting all admins...");
        Page<Admin> initialPage = adminRepository.findAll(pageable);
        log.info("Found " + initialPage.getContent().size() + " elements (page " + pageable.getPageNumber()+1 + "/" + initialPage.getTotalPages() + ")");
        List<AdminDTO> listDTO = initialPage.getContent().stream().map(MappingUtils::fromAdminToDTO).collect(Collectors.toList());
        log.info("Turned admins from DB into DTOs");
        return new PageImpl<>(listDTO, pageable, initialPage.getTotalPages());
    }

    @Override
    public Page<AdminDTO> findAllByFiltersAndPage(FilterForm filters, Pageable pageable) throws IllegalAccessException {
        log.info("Getting admins by specified filters and page...");
        Page<Admin> initialPage = adminRepository.findAll(buildSpecFromFilters(filters), pageable);
        log.info("Found " + initialPage.getContent().size() + " elements (page " + pageable.getPageNumber()+1 + "/" + initialPage.getTotalPages() + ")");
        List<AdminDTO> listDTO = initialPage.getContent().stream().map(MappingUtils::fromAdminToDTO).collect(Collectors.toList());
        log.info("Turned admins from DB into DTOs");
        return new PageImpl<>(listDTO, pageable, initialPage.getTotalPages());
    }

    @Override
    public Long countAllMasters() {

        Specification<Admin> spec = Specification.not(AdminSpecifications.hasRole(UserRole.ROLE_ADMIN)
                                                .and(AdminSpecifications.hasRole(UserRole.ROLE_MANAGER))
                                                .and(AdminSpecifications.hasRole(UserRole.ROLE_DIRECTOR))
                                                .and(AdminSpecifications.hasRole(UserRole.ROLE_ACCOUNTANT)));

        return adminRepository.count(spec);
    }

    @Override
    public Admin saveAdmin(Admin admin) {
        log.info("Trying to save admin...");
        log.info(admin.toString());
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        if(admin.getDateOfRegistry() == null) admin.setDateOfRegistry(LocalDate.now());
        log.info("Encoded admin password for saving");
        try {
            Admin savedAdmin = adminRepository.save(admin);
            log.info("Save successful!");
            log.info(savedAdmin.toString());

//            String currentlyLoggedInAdminEmail = ((CustomAdminDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
//
//            if(savedAdmin.getEmail().equalsIgnoreCase(currentlyLoggedInAdminEmail)) {
//                log.info("Updated currently logged user's credentials, changing role to " + savedAdmin.getRole().name());
//                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//                List<GrantedAuthority> updatedAuthorities = List.of(new SimpleGrantedAuthority(savedAdmin.getRole().name()));
//                Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
//                SecurityContextHolder.getContext().setAuthentication(newAuth);
//            }

            return savedAdmin;
        } catch (Exception e) {
            log.severe("Something went wrong during saving!");
            log.severe(e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteAdminById(Long id) {
        log.info("Trying to delete admin with ID: " + id);
        try {
            adminRepository.deleteById(id);
            log.info("Delete successful");
        } catch (Exception e) {
            log.severe("Something went wrong during deletion!");
            log.severe(e.getMessage());
        }
    }

    @Override
    public List<AdminDTO> findAllMasters(String search, Integer page) {
        Pageable pageable = PageRequest.of(page, 5);

        Specification<Admin> spec = Specification.not(AdminSpecifications.hasRole(UserRole.ROLE_ADMIN)
                        .and(AdminSpecifications.hasRole(UserRole.ROLE_MANAGER))
                        .and(AdminSpecifications.hasRole(UserRole.ROLE_DIRECTOR))
                        .and(AdminSpecifications.hasRole(UserRole.ROLE_ACCOUNTANT)))
                .and(AdminSpecifications.hasNameLike(search));

        return adminRepository.findAll(spec, pageable)
                .stream()
                .map(admin -> new AdminDTO(admin.getId(), admin.getFirst_name(), admin.getLast_name()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Admin> getAdminsByRole(UserRole role) { return adminRepository.getAdminsByRole(role);}

    @Override
    public Specification<Admin> buildSpecFromFilters(FilterForm filters) throws IllegalAccessException {

        if(!filters.filtersPresent()) return null;

        log.info("Building specification from filters: " + filters.toString());

        String name = filters.getName();
        UserRole role = (filters.getRole() != null) ? UserRole.valueOf(filters.getRole()) : null;
        String phone = filters.getPhone();
        String email = filters.getEmail();
        Boolean active = filters.getActive();

        Specification<Admin> specification = Specification.where(AdminSpecifications.hasNameLike(name)
                                                            .and(AdminSpecifications.hasRole(role))
                                                            .and(AdminSpecifications.hasPhoneLike(phone))
                                                            .and(AdminSpecifications.hasEmailLike(email))
                                                            .and(AdminSpecifications.isActive(active)));

        log.info("Specification built! " + specification);

        return specification;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Searching for user with username " + username);
        Admin admin = adminRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Такого пользователя не существует"));
        log.info("Admin found : " + admin.toString());
        return admin;
    }
}
