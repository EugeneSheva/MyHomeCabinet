package com.example.myhome.home.mapper;

import com.example.myhome.home.dto.AdminDTO;
import com.example.myhome.home.model.Admin;
import org.springframework.stereotype.Component;

@Component
public class AdminDTOMapper {

    public Admin fromDTOToAdmin(AdminDTO dto) {
        if(dto == null) return null;

        Admin admin = new Admin();
        admin.setId(dto.getId());
        admin.setFirst_name(dto.getFirst_name());
        admin.setLast_name(dto.getLast_name());
        admin.setFull_name(dto.getFullName());
        admin.setEmail(dto.getEmail());
        admin.setPassword(dto.getPassword());
        admin.setPhone_number(dto.getPhone_number());

        return admin;
    }
    public AdminDTO fromAdminToDTO(Admin admin) {
        if(admin == null) return null;

        AdminDTO dto = new AdminDTO();
        dto.setId(admin.getId());
        dto.setFirst_name(admin.getFirst_name());
        dto.setLast_name(admin.getLast_name());
        dto.setText(admin.getFullName());
        dto.setEmail(admin.getEmail());
        dto.setRole(admin.getRole().getName());
        dto.setUserRoleID(admin.getRole().getId());
        dto.setPhone_number(admin.getPhone_number());

        return dto;
    }

}
