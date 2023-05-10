package com.example.myhome.home.service.impl;

import com.example.myhome.home.model.UserRole;
import com.example.myhome.home.repository.UserRoleRepository;
import com.example.myhome.home.service.RoleService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log
public class RoleServiceImpl implements RoleService {

    @Autowired private UserRoleRepository repository;

    public List<UserRole> findAllManagerRoles() {
        return List.of(repository.findByName("Директор").orElse(null),
                repository.findByName("Администратор").orElse(null),
                repository.findByName("Управляющий").orElse(null));
    }

}
