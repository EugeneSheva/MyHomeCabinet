package com.example.myhome.home.validator;

import com.example.myhome.home.model.MeterData;
import com.example.myhome.home.model.MeterData_;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MeterValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return MeterData.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors e) {
        MeterData meter = (MeterData) target;

        if(meter.getBuilding() == null) {
            System.out.println("Error found(building)");
            e.rejectValue(MeterData_.BUILDING, "building.empty", "Нужно указать дом");
        } else if(meter.getSection() == null) {
            System.out.println("Error found(section)");
            e.rejectValue(MeterData_.SECTION, "section.empty", "Нужно указать секцию!");
        } else if(meter.getApartment() == null) {
            System.out.println("Error found(apartment)");
            e.rejectValue(MeterData_.APARTMENT, "apartment.empty", "Нужно указать квартиру");
        }

        if(meter.getService() == null) {
            System.out.println("Error found(service)");
            e.rejectValue(MeterData_.SERVICE, "service.empty", "Нужно указать счётчик");
        }
        if(meter.getStatus() == null) {
            System.out.println("Error found(status)");
            e.rejectValue(MeterData_.STATUS, "status.empty", "Нужно указать статус");
        }
        if(meter.getCurrentReadings() == null) {
            System.out.println("Error found(readings)");
            e.rejectValue(MeterData_.CURRENT_READINGS, "currentReadings.empty", "Нужно указать показания счётчика");
        }
        else if(meter.getCurrentReadings() < 0) {
            e.rejectValue(MeterData_.CURRENT_READINGS, "currentReadings.empty", "Показание не может быть отрицательным!");
        }
    }
}
