package com.example.myhome.home.converter;

import com.example.myhome.home.dto.OwnerDTO;
import org.springframework.core.convert.converter.Converter;

public class OwnerDTOToStringConverter implements Converter<OwnerDTO, String> {
    @Override
    public String convert(OwnerDTO source) {
        return String.valueOf(source.getId());
    }
}
