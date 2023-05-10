package com.example.myhome.home.validator;

import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.model.Owner;
import com.example.myhome.home.service.OwnerService;
import com.example.myhome.home.service.registration.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class LoginRequestValidator implements Validator {

    @Autowired private OwnerService ownerService;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public boolean supports(Class<?> clazz) {
        return LoginRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LoginRequest request = (LoginRequest) target;
        Owner owner;
        try {
            owner = ownerService.findByLogin(request.getUsername());
            if(!passwordEncoder.matches(request.getPassword(), owner.getPassword())) {
                errors.rejectValue("password", "password.no_match", "Wrong password!");
            }
        } catch (NotFoundException e) {
            errors.rejectValue("username", "username.nonexistent", "User with this email doesn't exist");
        }

    }
}
