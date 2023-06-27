package com.example.myhome.home.mapper;

import com.example.myhome.home.dto.OwnerDTO;
import com.example.myhome.home.model.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OwnerDTOMapper {
    @Autowired
    private final ApartmentDTOMapper apartmentMapper;

    public OwnerDTOMapper(ApartmentDTOMapper apartmentMapper) {
        this.apartmentMapper = apartmentMapper;
    }

    public Owner fromDTOToOwner(OwnerDTO dto){

        if(dto == null) return null;

        Owner owner = new Owner();
        owner.setId(dto.getId());
        owner.setFirst_name(dto.getFirst_name());
        owner.setLast_name(dto.getLast_name());
        owner.setFathers_name(dto.getFathers_name());
        owner.setEmail(dto.getEmail());

        return owner;
    }
    public OwnerDTO fromOwnerToDTO(Owner owner) {

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

    public OwnerDTO toDTOcabinetProfile(Owner owner) {

        OwnerDTO dto = new OwnerDTO();

        if(owner != null) {
            dto.setId(owner.getId());
            dto.setProfile_picture(owner.getProfile_picture());
            dto.setFirst_name(owner.getFirst_name());
            dto.setLast_name(owner.getLast_name());
            dto.setFathers_name(owner.getFathers_name());
            dto.setEmail(owner.getEmail());
            dto.setPhone_number(owner.getPhone_number());
            dto.setTelegram(owner.getTelegram());
            dto.setViber(owner.getViber());
            dto.setDescription(owner.getDescription());
            dto.setFullName(owner.getFirst_name() +" "+ owner.getFathers_name()+" "+owner.getLast_name());
            if (owner.getApartments() != null)dto.setApartments(apartmentMapper.listFromApartmentToDTOshort(owner.getApartments()));
            dto.setUnreadMessageQuantity((long) owner.getUnreadMessages().size());
        }
        return dto;
    }

    public OwnerDTO toDTOcabinetEditProfile(Owner owner) {

        OwnerDTO dto = new OwnerDTO();

        if(owner != null) {
            dto.setId(owner.getId());
            dto.setProfile_picture(owner.getProfile_picture());
            dto.setFirst_name(owner.getFirst_name());
            dto.setLast_name(owner.getLast_name());
            dto.setFathers_name(owner.getFathers_name());
            dto.setEmail(owner.getEmail());
            dto.setFullName(owner.getFirst_name() +" "+ owner.getFathers_name()+" "+owner.getLast_name());
            if (owner.getApartments() != null)dto.setApartments(apartmentMapper.listFromApartmentToDTOshort(owner.getApartments()));
            dto.setUnreadMessageQuantity((long) owner.getUnreadMessages().size());
            dto.setBirthdate(owner.getBirthdate());
            dto.setPhone_number(owner.getPhone_number());
            dto.setTelegram(owner.getTelegram());
            dto.setViber(owner.getViber());
            dto.setDescription(owner.getDescription());
            dto.setAdded_at(owner.getAdded_at());
        }
        return dto;
    }

    public Owner toEntityСabinetEditProfile(OwnerDTO dto) {
        Owner owner = new Owner();

        if (dto != null) {
            owner.setId(dto.getId());
            owner.setProfile_picture(dto.getProfile_picture());
            owner.setFirst_name(dto.getFirst_name());
            owner.setLast_name(dto.getLast_name());
            owner.setFathers_name(dto.getFathers_name());
            owner.setEmail(dto.getEmail());
            owner.setBirthdate(dto.getBirthdate());
            owner.setPhone_number(dto.getPhone_number());
            owner.setTelegram(dto.getTelegram());
            owner.setViber(dto.getViber());
            owner.setDescription(dto.getDescription());
            owner.setAdded_at(dto.getAdded_at());
        }
        return owner;
    }


//    public static Owner fromDTOcabinetProfile(OwnerDTO ownerDTO) {
//
//        OwnerDTO dto = new OwnerDTO();
//
//        if(owner != null) {
//            dto.setId(owner.getId());
//            dto.setFirst_name(owner.getFirst_name());
//            dto.setLast_name(owner.getLast_name());
//            dto.setFathers_name(owner.getFathers_name());
//            dto.setEmail(owner.getEmail());
//            dto.setFullName(String.join(" ", owner.getFirst_name(), owner.getFathers_name(), owner.getLast_name()));
//        }
//        return dto;
//    }
}
