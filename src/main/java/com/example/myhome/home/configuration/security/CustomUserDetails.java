package com.example.myhome.home.configuration.security;

import com.example.myhome.home.model.Admin;
import com.example.myhome.home.model.Owner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private Owner owner;

    public CustomUserDetails() {
    }

    public CustomUserDetails(Owner owner) {
        this.owner = owner;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return owner.getPassword();
    }

    @Override
    public String getUsername() {
        return owner.getEmail();
    }

    public Owner getOwner() {return this.owner;}

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
        return this.owner.isEnabled();
    }

//    @Override
//    public String toString() {
//        return "CustomUserDetails{" +
//                "owner=" + owner +
//                '}';
//    }
}
