package com.example.myhome.home.validator;

import com.example.myhome.home.model.Building;
import com.example.myhome.home.model.Owner;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.util.regex.Pattern;

@Component
public class OwnerValidator implements Validator {


        public boolean supports(Class clazz) {
            return Owner.class.equals(clazz);
        }

    @Override
    public void validate(Object obj, Errors e) {
        Owner owner = (Owner) obj;
        if (owner.getFirst_name() == null ||  owner.getFirst_name().isEmpty()) {
            e.rejectValue("first_name", "first_name.empty", "Заполните поле");
        } else if  (owner.getFirst_name().length()<2) {
            e.rejectValue("first_name", "first_name.empty", "Поле должно быть минимум 2 символа");
        }  else if  (owner.getFirst_name().length()>50) {
            e.rejectValue("first_name", "first_name.empty", "Слишком длинный текст");
        }
        if (owner.getLast_name() == null ||  owner.getLast_name().isEmpty()) {
            e.rejectValue("last_name", "last_name.empty", "Заполните поле");
        } else if  (owner.getLast_name().length()<2) {
            e.rejectValue("last_name", "last_name.empty", "Поле должно быть минимум 2 символа");
        } else if  (owner.getLast_name().length()>50) {
            e.rejectValue("last_name", "last_name.empty", "Слишком длинный текст");
        }
        if (owner.getFathers_name().length()>50) {
            e.rejectValue("fathers_name", "fathers_name.empty", "Слишком длинный текст");
        }
//        if (owner.getFathers_name() == null ||  owner.getFathers_name().isEmpty()) {
//            e.rejectValue("fathers_name", "fathers_name.empty", "Заполните поле");
//        } else if  (owner.getFathers_name().length()<2) {
//            e.rejectValue("fathers_name", "fathers_name.empty", "Поле должно быть минимум 2 символа");
//        }
        if (owner.getBirthdate() == null) {
            e.rejectValue("birthdate", "birthdate.empty", "Заполните поле");
        } else if  (owner.getBirthdate().isAfter(LocalDate.now().minusYears(18L))) {
            e.rejectValue("birthdate", "birthdate.empty", "Пользователь должен быть совершеннолетним");
        } else if  (owner.getBirthdate().isBefore(LocalDate.now().minusYears(120))) {
            e.rejectValue("birthdate", "birthdate.empty", "Введите актуальную дату");
        }

        if (owner.getPhone_number() == null ||  owner.getPhone_number().isEmpty()) {
            e.rejectValue("phone_number", "phone_number.empty", "Заполните поле");
        } else if  (owner.getPhone_number().length()!=10) {
            e.rejectValue("phone_number", "phone_number.empty", "Размер поля 10 символов. Пример \"0630636363\".");
        }

        if (owner.getViber() != null && owner.getViber().length()!=10) {
            e.rejectValue("viber", "viber.empty", "Размер поля 10 символов");
        }

        if (owner.getTelegram() != null && owner.getTelegram().length()!=10) {
            e.rejectValue("telegram", "telegram.empty", "Размер поля 10 символов");
        }

        if (owner.getEmail() == null ||  owner.getEmail().isEmpty()) {
            e.rejectValue("email", "email.empty", "Заполните поле");
        } else if  (!isValidEmailAdress(owner.getEmail()) ) {
            e.rejectValue("email", "email.empty", "Неверный формат Email.");
        } else if  (owner.getEmail().length() > 50) {
            e.rejectValue("email", "email.empty", "Слишком длинный текст.");
        }
        if (owner.getDescription() != null && owner.getDescription().length()>200) {
            e.rejectValue("description", "description.empty", "Слишком длинный текст");
        }
        if (owner.getPassword() != null && owner.getPassword().length()>50) {
            e.rejectValue("password", "password.empty", "Слишком длинный текст");
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
