package com.example.myhome.home.service;

import com.example.myhome.home.model.PageRoleDisplay;
import com.example.myhome.home.model.UserRole;

import java.util.List;

public interface UserRoleService {

    UserRole getRole(Long role_id);
    UserRole saveRole(UserRole role);
    void deleteRole(Long role_id);

    void updateRoles(List<PageRoleDisplay> list);

}
