package com.example.myhome.home.converter;

import com.example.myhome.home.dto.BuildingDTO;
import org.springframework.core.convert.converter.Converter;

public class StringToBuildingDTOConverter implements Converter<String, BuildingDTO> {
    @Override
    public BuildingDTO convert(String source) {
        BuildingDTO dto = new BuildingDTO();
        try {
            Long buildingID = Long.parseLong(source);
            dto.setId(buildingID);
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(dto);
        return dto;
    }
}
