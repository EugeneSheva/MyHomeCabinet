package com.example.myhome.home.service;

import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.model.Admin;
import com.example.myhome.home.model.AdminDTO;

import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.AdminRepository;

import com.example.myhome.home.repository.specifications.AdminSpecifications;
import com.example.myhome.util.UserRole;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;

@Service
@RequiredArgsConstructor
public class AdminService {


    private final AdminRepository adminRepository;
;


    public Admin findAdminById (Long id) { return adminRepository.findById(id).orElseThrow(NotFoundException::new);}

    public List<Admin> findAll() { return adminRepository.findAll(); }
    public List<Admin> findAll(int page_number) {return adminRepository.findAll(PageRequest.of(page_number, 5)).toList();}

    public List<Admin> findAllBySpecification(FilterForm filters) {

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

    public Admin saveAdmin(Admin admin) { return adminRepository.save(admin); }

    public void deleteAdminById(Long id) { adminRepository.deleteById(id); }

    public List<Admin> getAdminsByRole(UserRole role) { return adminRepository.getAdminsByRole(role);}



}
