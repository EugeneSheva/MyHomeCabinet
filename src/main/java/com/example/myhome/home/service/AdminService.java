package com.example.myhome.home.service;

import com.example.myhome.home.configuration.security.CustomAdminDetails;
import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.model.Admin;
import com.example.myhome.home.model.AdminDTO;

import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.AdminRepository;

import com.example.myhome.home.repository.specifications.AdminSpecifications;
import com.example.myhome.util.UserRole;
import lombok.RequiredArgsConstructor;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log
public class AdminService {


    @Autowired private final AdminRepository adminRepository;
    @Autowired private final PasswordEncoder passwordEncoder;


    public Admin findAdminById (Long id) { return adminRepository.findById(id).orElseThrow(NotFoundException::new);}
    public Admin findAdminByLogin(String login) {return adminRepository.findByEmail(login).orElseThrow(NotFoundException::new);}

    public List<Admin> findAll() { return adminRepository.findAll(); }
    public List<Admin> findAll(int page_number) {return adminRepository.findAll(PageRequest.of(page_number, 5)).toList();}

    public List<Admin> findAllBySpecification(FilterForm filters) {

        String name = filters.getName();
        UserRole role = (filters.getRole() != null && !filters.getRole().isEmpty()) ? UserRole.valueOf(filters.getRole()) : null;
        String phone = filters.getPhone();
        String email = filters.getEmail();
        Boolean active = filters.getActive();

        Specification<Admin> specification = Specification.where(AdminSpecifications.hasNameLike(name)
                                                            .and(AdminSpecifications.hasRole(role))
                                                            .and(AdminSpecifications.hasPhoneLike(phone))
                                                            .and(AdminSpecifications.hasEmailLike(email))
                                                            .and(AdminSpecifications.isActive(active)));

        return adminRepository.findAll(specification);
    }

    public List<Admin> findAllBySpecificationAndPage(FilterForm filters, Integer page_number) {
        Pageable page = PageRequest.of(Math.toIntExact(page_number), 5);

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

        return adminRepository.findAll(specification, page).toList();
    }

    public List<AdminDTO> findAllDTO() {
        List<AdminDTO>adminDTOList=new ArrayList<>();
        for (Admin admin : adminRepository.findAll()) {
            adminDTOList.add(new AdminDTO(admin.getId(),admin.getFirst_name(),admin.getLast_name(),admin.getPhone_number(),admin.getEmail(),admin.isActive(), admin.getRole()));
        }
        return adminDTOList;
    }

    public List<AdminDTO> findAllMasters(String search, int page){
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

    public Long countAllMasters() {

        Specification<Admin> spec = Specification.not(AdminSpecifications.hasRole(UserRole.ROLE_ADMIN)
                                                .and(AdminSpecifications.hasRole(UserRole.ROLE_MANAGER))
                                                .and(AdminSpecifications.hasRole(UserRole.ROLE_DIRECTOR))
                                                .and(AdminSpecifications.hasRole(UserRole.ROLE_ACCOUNTANT)));

        return adminRepository.count(spec);
    }

    public Admin saveAdmin(Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }


    public void deleteAdminById(Long id) { adminRepository.deleteById(id); }

    public List<Admin> getAdminsByRole(UserRole role) { return adminRepository.getAdminsByRole(role);}

    public Admin fromCustomAdminDetailsToAdmin(CustomAdminDetails details) {
        return findAdminByLogin(details.getUsername());
    }



}
