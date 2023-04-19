package com.example.myhome.home.configuration.security;

import com.example.myhome.home.model.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CustomAdminDetails implements UserDetails {

    private Admin admin;

    public CustomAdminDetails() {
    }

    public CustomAdminDetails(Admin admin) {
        this.admin = admin;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println(admin.getRole().name());
        return Collections.singletonList(new SimpleGrantedAuthority(admin.getRole().name()));
    }

    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    @Override
    public String getUsername() {
        return admin.getEmail();
    }

    public Admin getAdmin() {return this.admin;}

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "CustomAdminDetails{" +
                "admin=" + admin +
                '}';
    }
}
