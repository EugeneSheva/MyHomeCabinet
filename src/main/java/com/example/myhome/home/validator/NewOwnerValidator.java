package com.example.myhome.home.validator;

import com.example.myhome.home.dto.newOwnerDTO;
import com.example.myhome.home.model.Owner;
import com.example.myhome.home.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class NewOwnerValidator implements Validator {
    private final OwnerService ownerService;

        public boolean supports(Class clazz) {
            return newOwnerDTO.class.equals(clazz);
        }

    @Override
    public void validate(Object obj, Errors e) {
        newOwnerDTO owner = (newOwnerDTO) obj;
        if (owner.getFirst_name() == null ||  owner.getFirst_name().isEmpty()) {
            e.rejectValue("first_name", "first_name.empty", "Заполните поле");
        } else if  (owner.getFirst_name().length()<2) {
            e.rejectValue("first_name", "first_name.empty", "Поле должно быть минимум 2 символа");
        }
        if (owner.getLast_name() == null ||  owner.getLast_name().isEmpty()) {
            e.rejectValue("last_name", "last_name.empty", "Заполните поле");
        } else if  (owner.getLast_name().length()<2) {
            e.rejectValue("last_name", "last_name.empty", "Поле должно быть минимум 2 символа");
        }//
        if (owner.getEmail() == null ||  owner.getEmail().isEmpty()) {
            e.rejectValue("email", "email.empty", "Заполните поле");
        } else if  (!isValidEmailAdress(owner.getEmail()) ) {
            e.rejectValue("email", "email.empty", "Неверный формат Email.");
        } else if (ownerService.isOwnerExistsByEmail(owner.getEmail()) == true) {
            e.rejectValue("email", "email.empty", "Пользователь с таким E-mail существует.");
        }
        if (owner.getPassword() == null ||  owner.getPassword().isEmpty()) {
            e.rejectValue("password", "password.empty", "Заполните поле");
        } else if (owner.getPassword().length() < 8) {
            e.rejectValue("password", "password.empty", "Пароль должен быть не менее 8 символов.");
        } else if (!owner.getPassword().equals(owner.getRepassword())) {
            e.rejectValue("password", "password.empty", "Пароли не совпадают.");
        }
        if (owner.getConfirm() == false) {
            e.rejectValue("confirm", "confirm.empty", "Ознакомтесь и согласитесь с политикой конфиденциальности.");
        }
    }

    private boolean isValidEmailAdress(String email) {
        Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9._-]+$");
        Pattern DOMAIN_PATTERN = Pattern.compile("^[A-Za-z0-9._-]+$");

        if (email == null || email.isBlank()) {
            return false;
        }

        String[] emailParts = email.split("@");
        if (emailParts.length != 2) {
            return false;
        }

        String username = emailParts[0];
        String domain = emailParts[1];

        if (username.length() > 25 || username.length() < 1 || !USERNAME_PATTERN.matcher(username).matches()) {
            return false;
        }
        if (domain.length() > 15 || domain.length() < 2 || !DOMAIN_PATTERN.matcher(domain).matches()) {
            return false;
        }
        return true;
    }

}
