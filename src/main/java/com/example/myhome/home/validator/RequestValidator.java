package com.example.myhome.home.validator;

import com.example.myhome.home.dto.RepairRequestDTO;
import com.example.myhome.home.model.RepairRequest;
import com.example.myhome.home.model.RepairRequest_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.LocaleResolver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class RequestValidator implements Validator {

    @Autowired private MessageSource messageSource;

    @Override
    public boolean supports(Class<?> clazz) {
        return RepairRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors e) {
        RepairRequest request = (RepairRequest) target;

        Locale locale = LocaleContextHolder.getLocale();


        if(request.getDescription() == null || request.getDescription().equalsIgnoreCase("")) {
            e.rejectValue("description", "description.empty", messageSource.getMessage("requests.description.empty", null, locale));
        }
        if(request.getBest_time_request() != null) {
            if(request.getBest_time_request().isBefore(LocalDateTime.now())) e.rejectValue("best_time_request", "best_time_request.incorrect", messageSource.getMessage("requests.best_time.incorrect", null, locale));
        }
    }
}
