package com.example.myhome.home.repository;

import com.example.myhome.home.model.Admin;
import com.example.myhome.util.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long>, JpaSpecificationExecutor<Admin> {

    List<Admin> getAdminsByRole(UserRole role);

}
