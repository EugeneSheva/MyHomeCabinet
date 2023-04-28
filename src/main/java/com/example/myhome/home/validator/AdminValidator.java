package com.example.myhome.home.validator;

import com.example.myhome.home.dto.AdminDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AdminValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return this.getClass().equals(clazz);
    }

    @Override
    public void validate(Object target, Errors e) {
        AdminDTO dto = (AdminDTO) target;
        if(dto.getFirst_name() == null || dto.getFirst_name().isEmpty()) {
            e.rejectValue("first_name", "first_name.empty", "Имя не может быть пустым");
        }
        if(dto.getLast_name() == null || dto.getLast_name().isEmpty()) {
            e.rejectValue("last_name", "last_name.empty", "Фамилия не может быть пустой");
        }
        if(dto.getPhone_number() == null || dto.getPhone_number().isEmpty()) {
            e.rejectValue("phone_number", "phone_number.empty", "Телефон не может быть пустым!");
        }
        if(dto.getEmail() == null || dto.getEmail().isEmpty()) {
            e.rejectValue("email", "email.empty", "");
        }

    }

}
