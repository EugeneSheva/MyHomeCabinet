package com.example.myhome.home.mapper;

import com.example.myhome.home.dto.OwnerDTO;
import com.example.myhome.home.model.Owner;
import org.springframework.stereotype.Component;

@Component
public class OwnerDTOMapper {

    public static Owner fromDTOToOwner(OwnerDTO dto){

        if(dto == null) return null;

        Owner owner = new Owner();
        owner.setId(dto.getId());
        owner.setFirst_name(dto.getFirst_name());
        owner.setLast_name(dto.getLast_name());
        owner.setFathers_name(dto.getFathers_name());
        owner.setEmail(dto.getEmail());

        return owner;
    }
    public static OwnerDTO fromOwnerToDTO(Owner owner) {

        OwnerDTO dto = new OwnerDTO();

        if(owner != null) {
            dto.setId(owner.getId());
            dto.setFirst_name(owner.getFirst_name());
            dto.setLast_name(owner.getLast_name());
            dto.setFathers_name(owner.getFathers_name());
            dto.setEmail(owner.getEmail());
            dto.setFullName(String.join(" ", owner.getFirst_name(), owner.getFathers_name(), owner.getLast_name()));
        }

        return dto;
    }

}
