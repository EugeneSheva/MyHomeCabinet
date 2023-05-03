package com.example.myhome.home.mapper;

import com.example.myhome.home.dto.ApartmentDTO;
import com.example.myhome.home.model.Apartment;
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

//        apartment.setBuilding(fromDTOToBuilding(dto.getBuilding()));
//        apartment.setAccount(fromDTOToAccount(dto.getAccount()));
//        apartment.setOwner(fromDTOToOwner(dto.getOwner()));

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

//        dto.setBuilding(fromBuildingToDTO(apartment.getBuilding()));
//        dto.setAccount(fromAccountToDTO(apartment.getAccount()));
//        dto.setOwner(fromOwnerToDTO(apartment.getOwner()));

        return dto;
    }

}
