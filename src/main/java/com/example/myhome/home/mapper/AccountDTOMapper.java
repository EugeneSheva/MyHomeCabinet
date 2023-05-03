package com.example.myhome.home.mapper;

import com.example.myhome.home.dto.ApartmentAccountDTO;
import com.example.myhome.home.dto.ApartmentDTO;
import com.example.myhome.home.dto.BuildingDTO;
import com.example.myhome.home.dto.OwnerDTO;
import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.model.ApartmentAccount;
import com.example.myhome.home.model.Building;
import com.example.myhome.home.model.Owner;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

@Component
@Log
public class AccountDTOMapper {

    public ApartmentAccount fromDTOToAccount(ApartmentAccountDTO dto) {

        if(dto == null) return null;

        ApartmentAccount account = new ApartmentAccount();
        account.setId(dto.getId());
        account.setIsActive(dto.getIsActive());
        account.setChangedState(dto.getChangedState());
        account.setSection(dto.getSection());
        account.setBalance(dto.getBalance());

//        account.setApartment((dto.getApartment() != null) ? fromDTOToApartment(dto.getApartment()) : null);
//        account.setBuilding((dto.getBuilding() != null) ? fromDTOToBuilding(dto.getBuilding()) : null);
//        account.setOwner((dto.getOwner() != null) ? fromDTOToOwner(dto.getOwner()) : null);

        return account;
    }
    public ApartmentAccountDTO fromAccountToDTO(ApartmentAccount account) {

        if(account == null) return null;

        ApartmentAccountDTO dto = new ApartmentAccountDTO();
        dto.setId(account.getId());
        dto.setIsActive(account.getIsActive());
        dto.setBalance(account.getBalance());
        dto.setChangedState(account.getChangedState());
        dto.setSection(account.getSection());

        Apartment apartment = account.getApartment();
        if(apartment != null) {
            dto.setApartment(
                    ApartmentDTO.builder()
                            .id(apartment.getId())
                            .fullName("кв. " + apartment.getNumber() + ", " + account.getBuilding().getName())
                            .build());
        }

        Building building = account.getBuilding();
        if(building != null) {
            dto.setBuilding(
                    BuildingDTO.builder()
                            .id(building.getId())
                            .name(building.getName())
                            .build()
            );
        }

        Owner owner = account.getApartment().getOwner();
        if(owner != null) {
            dto.setOwner(
                    OwnerDTO.builder()
                            .id(owner.getId())
                            .fullName(owner.getFullName())
                            .build()
            );
        }

//        dto.setApartment(fromApartmentToDTO(account.getApartment()));
//        dto.setOwner(fromOwnerToDTO(account.getOwner()));

        log.info("Created dto: ");
        log.info(dto.toString());

        return dto;
    }

}
