package com.example.myhome.home.service;

import com.example.myhome.home.dto.AdminDTO;
import com.example.myhome.home.model.Admin;
import com.example.myhome.home.model.UserRole;
import com.example.myhome.home.model.filter.FilterForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AdminService extends UserDetailsService {

    Admin findAdminById(Long admin_id);
    Admin findAdminByLogin(String login);

}
