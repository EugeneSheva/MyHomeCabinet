package com.example.myhome.home.validator;

import com.example.myhome.home.model.Building;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class BuildingValidator implements Validator {


        public boolean supports(Class clazz) {
            return Building.class.equals(clazz);
        }

    @Override
    public void validate(Object obj, Errors e) {
        ValidationUtils.rejectIfEmpty(e, "name", "name.empty");
        Building building = (Building) obj;
        if (building.getName() == null ||  building.getName().isEmpty()) {
            e.rejectValue("name", "name.empty", "Заполните поле");
        }
        if (building.getAddress() == null ||  building.getAddress().isEmpty()) {
            e.rejectValue("address", "address.empty", "Заполните поле");
        } else if  (building.getAddress().length()<10) {
            e.rejectValue("address", "address.empty", "Поле должно быть минимум 10 символов");
        }
    }
}
