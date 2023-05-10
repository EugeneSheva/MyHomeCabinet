package com.example.myhome.home.validator;

import com.example.myhome.home.dto.MeterDataDTO;
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
        MeterDataDTO meter = (MeterDataDTO) target;

        if(meter.getDate().isAfter(LocalDate.now())) {
            e.rejectValue("date", "date.incorrect", "Дата не может быть в будущем");
        }

        if(meter.getBuildingID() == null) {
            System.out.println("Error found(building)");
            e.rejectValue("buildingID", "building.empty", "Выберите дом!");
        } else if(meter.getSection() == null || meter.getSection().equals("")) {
            System.out.println("Error found(section)");
            e.rejectValue("section", "section.empty", "Выберите секцию!");
        } else if(meter.getApartmentID() == null) {
            System.out.println("Error found(apartment)");
            e.rejectValue("apartmentID", "apartment.empty", "Выберите квартиру!");
        }

        if(meter.getServiceID() == null) {
            System.out.println("Error found(service)");
            e.rejectValue("serviceID", "service.empty", "Выберите счётчик!");
        }
        if(meter.getStatus() == null) {
            System.out.println("Error found(status)");
            e.rejectValue("status", "status.empty", "Выберите статус!");
        }
        if(meter.getReadings() == null) {
            System.out.println("Error found(readings)");
            e.rejectValue("readings", "currentReadings.empty", "Укажите показания счётчика!");
        }
        else if(meter.getReadings() < 0) {
            e.rejectValue("readings", "currentReadings.empty", "Показания не могут быть отрицательными");
        }
    }
}
