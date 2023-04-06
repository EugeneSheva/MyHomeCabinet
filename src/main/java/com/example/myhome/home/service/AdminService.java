package com.example.myhome.home.service;

import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.model.Admin;
import com.example.myhome.home.model.AdminDTO;

import com.example.myhome.home.repository.AdminRepository;

import com.example.myhome.util.UserRole;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {


    private final AdminRepository adminRepository;
;


    public Admin findAdminById (Long id) { return adminRepository.findById(id).orElseThrow(NotFoundException::new);}

    public List<Admin> findAll() { return adminRepository.findAll(); }

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
