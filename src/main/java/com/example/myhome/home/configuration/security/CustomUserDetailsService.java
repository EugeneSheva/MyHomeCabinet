package com.example.myhome.home.configuration.security;

import com.example.myhome.home.model.Owner;
import com.example.myhome.home.repository.OwnerRepository;
import com.example.myhome.home.service.AdminService;
import com.example.myhome.home.service.OwnerService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired private OwnerRepository ownerRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Owner> owner = ownerRepository.findByEmail(login);
        if(owner.isEmpty()) {
            throw new UsernameNotFoundException("No user found with the given email");
        }
        return new CustomUserDetails(owner.get());
    }
}
