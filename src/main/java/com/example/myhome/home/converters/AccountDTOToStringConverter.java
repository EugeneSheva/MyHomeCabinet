package com.example.myhome.home.converters;

import com.example.myhome.home.dto.ApartmentAccountDTO;
import org.springframework.core.convert.converter.Converter;

public class AccountDTOToStringConverter implements Converter<ApartmentAccountDTO, String> {
    @Override
    public String convert(ApartmentAccountDTO source) {
        return String.valueOf(source.getId());
    }
}
