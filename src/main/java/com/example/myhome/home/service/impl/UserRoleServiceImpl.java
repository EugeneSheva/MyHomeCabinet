package com.example.myhome.home.service.impl;

import com.example.myhome.home.model.UserRole;
import com.example.myhome.home.repository.UserRoleRepository;
import com.example.myhome.home.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

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

    public void deleteRoleById(Long role_id) {
        repository.deleteById(role_id);
    }

}
