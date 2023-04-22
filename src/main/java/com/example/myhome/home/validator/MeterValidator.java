package com.example.myhome.home.validator;

import com.example.myhome.home.model.MeterData;
import com.example.myhome.home.model.MeterData_;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
public class MeterValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return MeterData.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors e) {
        MeterData meter = (MeterData) target;

        if(meter.getDate().isAfter(LocalDate.now())) {
            e.rejectValue(MeterData_.DATE, "date.incorrect", "Дата не может быть в будущем");
        }

        if(meter.getBuilding() == null) {
            System.out.println("Error found(building)");
            e.rejectValue(MeterData_.BUILDING, "building.empty", "Выберите дом!");
        } else if(meter.getSection() == null || meter.getSection().equals("")) {
            System.out.println("Error found(section)");
            e.rejectValue(MeterData_.SECTION, "section.empty", "Выберите секцию!");
        } else if(meter.getApartment() == null) {
            System.out.println("Error found(apartment)");
            e.rejectValue(MeterData_.APARTMENT, "apartment.empty", "Выберите квартиру!");
        }

        if(meter.getService() == null) {
            System.out.println("Error found(service)");
            e.rejectValue(MeterData_.SERVICE, "service.empty", "Выберите счётчик!");
        }
        if(meter.getStatus() == null) {
            System.out.println("Error found(status)");
            e.rejectValue(MeterData_.STATUS, "status.empty", "Выберите статус!");
        }
        if(meter.getCurrentReadings() == null) {
            System.out.println("Error found(readings)");
            e.rejectValue(MeterData_.CURRENT_READINGS, "currentReadings.empty", "Укажите показания счётчика!");
        }
        else if(meter.getCurrentReadings() < 0) {
            e.rejectValue(MeterData_.CURRENT_READINGS, "currentReadings.empty", "Показания не могут быть отрицательными");
        }
    }
}
