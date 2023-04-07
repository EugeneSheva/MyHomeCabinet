package com.example.myhome.home.validator;

import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.model.Building;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ApartmentValidator implements Validator {


        public boolean supports(Class clazz) {
            return Apartment.class.equals(clazz);
        }

    @Override
    public void validate(Object obj, Errors e) {
        Apartment apartment = (Apartment) obj;
        if (apartment.getNumber() == null) {
            e.rejectValue("number", "number.empty", "Заполните поле");
        }
        if (apartment.getSquare() == null) {
            e.rejectValue("square", "square.empty", "Заполните поле");
        }
        if (apartment.getBuilding() == null) {
            e.rejectValue("building", "building.empty", "Заполните поле");
        }
        if (apartment.getFloor() == null) {
            e.rejectValue("floor", "floor.empty", "Заполните поле");
        }
        if (apartment.getSection() == null) {
            e.rejectValue("section", "section.empty", "Заполните поле");
        }
        if (apartment.getOwner() == null) {
            e.rejectValue("owner", "owner.empty", "Заполните поле");
        }

    }
}
