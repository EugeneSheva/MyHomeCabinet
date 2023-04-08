package com.example.myhome.home.validator;

import com.example.myhome.home.model.ApartmentAccount;
import com.example.myhome.home.model.ApartmentAccount_;
import com.example.myhome.home.repository.ApartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AccountValidator implements Validator {

    @Autowired private ApartmentRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        return ApartmentAccount.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ApartmentAccount account = (ApartmentAccount) target;
        if(account.getApartment() == null) {
            errors.rejectValue(ApartmentAccount_.APARTMENT, "apartment.empty", "Укажите квартиру!");
        }
//        else if(repository.findById(account.getApartment().getId()).orElseThrow().getAccount() != null) {
//            errors.rejectValue(ApartmentAccount_.APARTMENT, "apartment.empty", "К этой квартире уже привязан лицевой счет!");
//        }
        if(account.getIsActive() == null) {
            errors.rejectValue(ApartmentAccount_.IS_ACTIVE, "isActive.empty", "Необходимо указать статус");
        }
    }
}
