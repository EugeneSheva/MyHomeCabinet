package com.example.myhome.home.configuration.security;

import com.example.myhome.home.model.Admin;
import com.example.myhome.home.model.Owner;
import com.example.myhome.home.repository.AdminRepository;
import com.example.myhome.home.repository.OwnerRepository;
import com.example.myhome.home.service.AdminService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log
public class CustomAdminDetailsService implements UserDetailsService {

    @Autowired private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Admin> admin = adminRepository.findByEmail(login);
        if(admin.isEmpty()) {
            throw new UsernameNotFoundException("No admin found with the given email");
        }
        log.info(admin.toString());
        return new CustomAdminDetails(admin.get());
    }
}
