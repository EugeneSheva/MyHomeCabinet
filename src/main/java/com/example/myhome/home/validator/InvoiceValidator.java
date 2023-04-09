package com.example.myhome.home.validator;

import com.example.myhome.home.model.Invoice;
import com.example.myhome.home.model.Invoice_;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
public class InvoiceValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Invoice.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors e) {
        Invoice invoice = (Invoice) target;
        LocalDate now = LocalDate.now();
        if(invoice.getBuilding() == null) {
            e.rejectValue(Invoice_.BUILDING, "building.empty", "Нужно указать дом!");
        } else if(invoice.getSection() == null) {
            e.rejectValue(Invoice_.SECTION, "section.empty", "Нужно указать секцию!");
        } else if(invoice.getApartment() == null) {
            e.rejectValue(Invoice_.APARTMENT, "apartment.empty", "Нужно указать квартиру!");
        }
        if(invoice.getStatus() == null) {
            e.rejectValue(Invoice_.STATUS, "status.empty", "Нужно указать статус квитанции!");
        }
        if(invoice.getDateFrom() == null || invoice.getDateTo() == null) {
            e.rejectValue(Invoice_.DATE_FROM, "period.empty", "Укажите период!");
        } else if(invoice.getDateFrom().isAfter(now)) {
            e.rejectValue(Invoice_.DATE_FROM, "date_from.incorrect", "Неправильное начало периода!");
        } else if(invoice.getDateFrom().isAfter(invoice.getDateTo())) {
            e.rejectValue(Invoice_.DATE_TO, "date_to.incorrect", "Неправильный период квитанции!");
        }
//        if(invoice.getComponents() == null || invoice.getComponents().size() == 0) {
//            e.rejectValue(Invoice_.COMPONENTS, "components.empty", "Нельзя создать пустую услугу!");
//        }
    }
}
