package com.example.myhome.home.validator;

import com.example.myhome.home.model.RepairRequest;
import com.example.myhome.home.model.RepairRequest_;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;

@Component
public class RequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return RepairRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors e) {
        RepairRequest request = (RepairRequest) target;
        if(request.getOwner() == null) {
            e.rejectValue(RepairRequest_.OWNER, "ownerId.empty", "Необходимо указать владельца квартиры");
        } else if(request.getApartment() == null) {
            e.rejectValue(RepairRequest_.APARTMENT, "apartment.empty", "Необходимо выбрать квартиру");
        }

        if(request.getDescription() == null || request.getDescription().equalsIgnoreCase("")) {
            e.rejectValue(RepairRequest_.DESCRIPTION, "description.empty", "Опишите проблему!");
        }

        if(request.getMaster() == null) {
            e.rejectValue(RepairRequest_.MASTER, "master.empty", "Выберите мастера!");
        }

        if(request.getMaster_type() == null) {
            e.rejectValue(RepairRequest_.MASTER_TYPE, "master_type.empty", "Выберите тип мастера!");
        }

        if(request.getStatus() == null) {
            e.rejectValue(RepairRequest_.STATUS, "status.empty", "Выберите статус!");
        }
        if(request.getBest_time_request() != null && request.getBest_time_request().isBefore(LocalDateTime.now())) {
            e.rejectValue(RepairRequest_.BEST_TIME_REQUEST, "best_time.incorrect", "Время не может быть в прошлом");
        }
    }
}
