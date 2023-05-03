package com.example.myhome.home.validator;

import com.example.myhome.home.dto.ApartmentAccountDTO;
import com.example.myhome.home.model.ApartmentAccount_;
import com.example.myhome.home.service.impl.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AccountValidator implements Validator {

    @Autowired private AccountServiceImpl accountServiceImpl;

    @Override
    public boolean supports(Class<?> clazz) {
        return ApartmentAccountDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ApartmentAccountDTO account = (ApartmentAccountDTO) target;
        System.out.println("ALO DTO BLYAT");
        System.out.println(account);
        if(account.getBuilding() == null || account.getBuilding().getId() == null) {
            System.out.println("Error found(building)");
            errors.rejectValue(ApartmentAccount_.BUILDING, "building.empty", "Укажите дом!");
        } else if(account.getSection() == null || account.getSection().equalsIgnoreCase("0")) {
            System.out.println("Error found(section)");
            errors.rejectValue(ApartmentAccount_.SECTION, "section.empty", "Укажите секцию!");
        } else if(account.getApartment() == null || account.getApartment().getId() == 0) {
            System.out.println("Error found(apartment)");
            errors.rejectValue(ApartmentAccount_.APARTMENT, "apartment.empty", "Укажите квартиру!");
        } else if(accountServiceImpl.apartmentHasAccount(account.getApartment().getId())) {
            if(account.getChangedState() == null || !account.getChangedState()) errors.rejectValue(ApartmentAccount_.APARTMENT, "apartment.has_account", "К этой квартире уже привязан лицевой счёт!");
        }
        if(account.getIsActive() == null) {
            System.out.println("Error found(active)");
            errors.rejectValue(ApartmentAccount_.IS_ACTIVE, "isActive.empty", "Необходимо указать статус");
        }
    }
}
