package com.example.myhome.home.service;

import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.model.Admin;
import com.example.myhome.home.model.AdminDTO;
import com.example.myhome.home.model.Owner;
import com.example.myhome.home.model.OwnerDTO;
import com.example.myhome.home.repos.AdminRepository;
import com.example.myhome.home.repos.OwnerRepository;
import com.example.myhome.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {


    private final AdminRepository adminRepository;
;


    public Admin findById (Long id) { return adminRepository.findById(id).orElseThrow(() -> new NotFoundException());}

    public List<Admin> findAll() { return adminRepository.findAll(); }

    public List<AdminDTO> findAllDTO() {
        List<AdminDTO>adminDTOList=new ArrayList<>();
        for (Admin admin : adminRepository.findAll()) {
            adminDTOList.add(new AdminDTO(admin.getId(),admin.getFirst_name(),admin.getLast_name(),admin.getPhone_number(),admin.getEmail(),admin.isActive(), admin.getRole()));
        }
        return adminDTOList;
    }

    public Admin save(Admin admin) { return adminRepository.save(admin); }

    public void deleteById(Long id) { adminRepository.deleteById(id); }




}
