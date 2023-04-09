package com.example.myhome.home.validator;

import com.example.myhome.home.model.ApartmentAccount;
import com.example.myhome.home.model.ApartmentAccount_;
import com.example.myhome.home.repository.ApartmentRepository;
import com.example.myhome.home.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AccountValidator implements Validator {

    @Autowired private AccountService accountService;

    @Override
    public boolean supports(Class<?> clazz) {
        return ApartmentAccount.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ApartmentAccount account = (ApartmentAccount) target;
        System.out.println(account.getApartment().getId());
        if(account.getBuilding() == null) {
            System.out.println("Error found(building)");
            errors.rejectValue(ApartmentAccount_.BUILDING, "building.empty", "Укажите дом!");
        } else if(account.getSection() == null) {
            System.out.println("Error found(section)");
            errors.rejectValue(ApartmentAccount_.SECTION, "section.empty", "Укажите секцию!");
        } else if(account.getApartment() == null) {
            System.out.println("Error found(apartment)");
            errors.rejectValue(ApartmentAccount_.APARTMENT, "apartment.empty", "Укажите квартиру!");
        } else if(accountService.apartmentHasAccount(account.getApartment().getId())) {
            errors.rejectValue(ApartmentAccount_.APARTMENT, "apartment.has_account", "К этой квартире уже привязан лицевой счёт!");
        }
//        else if(repository.findById(account.getApartment().getId()).orElseThrow().getAccount() != null) {
//            errors.rejectValue(ApartmentAccount_.APARTMENT, "apartment.empty", "К этой квартире уже привязан лицевой счет!");
//        }
        if(account.getIsActive() == null) {
            System.out.println("Error found(active)");
            errors.rejectValue(ApartmentAccount_.IS_ACTIVE, "isActive.empty", "Необходимо указать статус");
        }
    }
}
