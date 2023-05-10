package com.example.myhome.home.mapper;

import com.example.myhome.home.dto.ApartmentAccountDTO;
import com.example.myhome.home.dto.ApartmentDTO;
import com.example.myhome.home.dto.BuildingDTO;
import com.example.myhome.home.dto.OwnerDTO;
import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.model.ApartmentAccount;
import com.example.myhome.home.model.Building;
import com.example.myhome.home.model.Owner;
import org.springframework.stereotype.Component;

@Component
public class ApartmentDTOMapper {

    public Apartment fromDTOToApartment(ApartmentDTO dto) {

        if(dto == null) return null;

        Apartment apartment = new Apartment();
        apartment.setId(dto.getId());
        apartment.setSection(dto.getSection());
        apartment.setFloor(dto.getFloor());
        apartment.setNumber(dto.getNumber());
        apartment.setBalance(dto.getBalance());
        apartment.setSquare(dto.getSquare());

        return apartment;
    }
    public ApartmentDTO fromApartmentToDTO(Apartment apartment) {

        if(apartment == null) return null;

        ApartmentDTO dto = new ApartmentDTO();
        dto.setId(apartment.getId());
        dto.setSection(apartment.getSection());
        dto.setFloor(apartment.getFloor());
        dto.setNumber(apartment.getNumber());
        dto.setBalance(apartment.getBalance());
        dto.setSquare(apartment.getSquare());
        dto.setFullName("кв. " + apartment.getNumber() + ", " + apartment.getBuilding().getName());

        Building building = apartment.getBuilding();
        if(building != null) {
            dto.setBuilding(BuildingDTO.builder()
                    .id(building.getId())
                    .name(building.getName())
                    .sections(building.getSections())
                    .build());
        }

        Owner owner = apartment.getOwner();
        if(owner != null) {
            dto.setOwner(OwnerDTO.builder()
                            .id(owner.getId())
                            .fullName(owner.getFullName())
                    .build());
        }

        ApartmentAccount account = apartment.getAccount();
        if(account != null) {
            dto.setAccount(ApartmentAccountDTO.builder()
                            .id(account.getId())
                    .build());
        }

        return dto;
    }

}
