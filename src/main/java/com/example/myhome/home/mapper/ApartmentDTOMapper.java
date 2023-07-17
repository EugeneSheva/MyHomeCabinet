package com.example.myhome.home.mapper;

import com.example.myhome.home.dto.ApartmentAccountDTO;
import com.example.myhome.home.dto.ApartmentDTO;
import com.example.myhome.home.dto.BuildingDTO;
import com.example.myhome.home.dto.OwnerDTO;
import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.model.ApartmentAccount;
import com.example.myhome.home.model.Building;
import com.example.myhome.home.model.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ApartmentDTOMapper {
    @Autowired
    private  BuildingDTOMapper buildingDTOMapper;


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

    public List<ApartmentDTO> listFromApartmentToDTOshort(List<Apartment> apartmentLIst) {
        List<ApartmentDTO>apartmentDTOList=new ArrayList<>();
        for (Apartment apartment : apartmentLIst) {
            ApartmentDTO dto = new ApartmentDTO();
            Building b = apartment.getBuilding();
            System.out.println(b);
            dto.setId(apartment.getId());
            dto.setSection(apartment.getSection());
            dto.setFloor(apartment.getFloor());
            dto.setNumber(apartment.getNumber());
            dto.setBalance(apartment.getBalance());
            dto.setSquare(apartment.getSquare());
            dto.setBuilding(buildingDTOMapper.fromBuildingToDTO(apartment.getBuilding()));
            dto.setFullName(b.getName() + " , " + b.getAddress() + " кв. " + apartment.getNumber() + "." );
            dto.setAccountNo(apartment.getAccount().getId());
            if (b.getImg1()!=null) dto.setImg1(b.getImg1());
            if (b.getImg2()!=null) dto.setImg2(b.getImg2());
            if (b.getImg3()!=null) dto.setImg3(b.getImg3());
            if (b.getImg4()!=null) dto.setImg4(b.getImg4());
            if (b.getImg5()!=null) dto.setImg5(b.getImg5());
            apartmentDTOList.add(dto);
        }
        System.out.println(apartmentDTOList);
    return apartmentDTOList;
    }

}
