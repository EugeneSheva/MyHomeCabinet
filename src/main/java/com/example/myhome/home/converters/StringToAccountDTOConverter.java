package com.example.myhome.home.converters;

import com.example.myhome.home.dto.ApartmentAccountDTO;
import com.example.myhome.home.dto.BuildingDTO;
import org.springframework.core.convert.converter.Converter;

public class StringToAccountDTOConverter implements Converter<String, ApartmentAccountDTO> {
    @Override
    public ApartmentAccountDTO convert(String source) {
        ApartmentAccountDTO dto = new ApartmentAccountDTO();
        System.out.println(source);
        try {
            Long accountID = Long.parseLong(source);
            dto.setId(accountID);
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(dto);
        return dto;
    }
}
