package com.example.myhome.home.service;

import com.example.myhome.home.model.Admin;
import com.example.myhome.home.repository.AdminRepository;
import com.example.myhome.util.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public List<Admin> findAll() {return adminRepository.findAll();}
    public List<Admin> getAdminsByRole(UserRole role) {return adminRepository.getAdminsByRole(role);}

    public Admin findAdminById(long admin_id) {return adminRepository.findById(admin_id).orElseThrow();}

    public Admin saveAdmin(Admin admin) {return adminRepository.save(admin);}

    public void deleteAdminById(long admin_id) {adminRepository.deleteById(admin_id);}

}
