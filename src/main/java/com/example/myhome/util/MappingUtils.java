package com.example.myhome.util;

import com.example.myhome.home.dto.*;
import com.example.myhome.home.model.*;

import java.util.List;
import java.util.stream.Collectors;

public class MappingUtils {

    //ACCOUNT
    public static ApartmentAccount fromDTOToAccount(ApartmentAccountDTO dto) {

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
    public static ApartmentAccountDTO fromAccountToDTO(ApartmentAccount account) {

        if(account == null) return null;

        ApartmentAccountDTO dto = new ApartmentAccountDTO();
        dto.setId(account.getId());
        dto.setIsActive(account.getIsActive());
        dto.setBalance(account.getBalance());
        dto.setNumber(account.getNumber());
        dto.setChangedState(account.getChangedState());
        dto.setSection(account.getSection());

//        dto.setApartment(fromApartmentToDTO(account.getApartment()));
//        dto.setOwner(fromOwnerToDTO(account.getOwner()));

        return dto;
    }

    //APARTMENT
    public static Apartment fromDTOToApartment(ApartmentDTO dto) {

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
    public static ApartmentDTO fromApartmentToDTO(Apartment apartment) {

        if(apartment == null) return null;

        ApartmentDTO dto = new ApartmentDTO();
        dto.setId(apartment.getId());
        dto.setSection(apartment.getSection());
        dto.setFloor(apartment.getFloor());
        dto.setNumber(apartment.getNumber());
        dto.setBalance(apartment.getBalance());
        dto.setSquare(apartment.getSquare());
//        dto.setFullName("кв. " + dto.getNumber() + ", " + dto.getBuilding().getName());

//        dto.setBuilding(fromBuildingToDTO(apartment.getBuilding()));
//        dto.setAccount(fromAccountToDTO(apartment.getAccount()));
//        dto.setOwner(fromOwnerToDTO(apartment.getOwner()));

        return dto;
    }

    //BUILDING
    public static Building fromDTOToBuilding(BuildingDTO dto) {

        if(dto == null) return null;

        Building building = new Building();
        building.setId(dto.getId());
        building.setName(dto.getName());
        building.setSections(dto.getSections());
        building.setAddress(dto.getAddress());
        building.setFloors(dto.getFloors());
//        building.setApartments(dto.getApartments().stream().map(MappingUtils::fromDTOToApartment).collect(Collectors.toList()));

        return building;
    }
    public static BuildingDTO fromBuildingToDTO(Building building) {

        if(building == null) return null;

        BuildingDTO dto = new BuildingDTO();
        dto.setId(building.getId());
        dto.setName(building.getName());
        dto.setSections(building.getSections());
        dto.setAddress(building.getAddress());
        dto.setFloors(building.getFloors());
//        dto.setApartments(building.getApartments().stream().map(MappingUtils::fromApartmentToDTO).collect(Collectors.toList()));

        return dto;
    }

    //OWNER
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

    //ADMIN
    public static Admin fromDTOToAdmin(AdminDTO dto) {
        if(dto == null) return null;

        Admin admin = new Admin();
        admin.setId(dto.getId());
        admin.setFirst_name(dto.getFirst_name());
        admin.setLast_name(dto.getLast_name());
        admin.setEmail(dto.getEmail());
        admin.setPassword(dto.getPassword());
        admin.setRole(dto.getRole());
        admin.setPhone_number(dto.getPhone_number());

        return admin;
    }
    public static AdminDTO fromAdminToDTO(Admin admin) {
        if(admin == null) return null;

        AdminDTO dto = new AdminDTO();
        dto.setId(admin.getId());
        dto.setFirst_name(admin.getFirst_name());
        dto.setLast_name(admin.getLast_name());
        dto.setEmail(admin.getEmail());
        dto.setRole(admin.getRole());
        dto.setPhone_number(admin.getPhone_number());

        return dto;
    }

    //METER
    public static MeterData fromDTOToMeter(MeterDataDTO dto) {
        if(dto == null) return null;
        MeterData meter = new MeterData();
        meter.setId(dto.getId());
        meter.setCurrentReadings(dto.getReadings());
        meter.setSection(dto.getSection());

        return meter;
    }
    public static MeterDataDTO fromMeterToDTO(MeterData meter) {
        if(meter == null) return null;
        MeterDataDTO dto = new MeterDataDTO();
        dto.setId(meter.getId());
        dto.setReadings(meter.getCurrentReadings());
        dto.setApartmentID(meter.getApartment().getId());
        dto.setApartmentNumber(meter.getApartment().getNumber());
        dto.setBuildingID(meter.getBuilding().getId());
        dto.setBuildingName(meter.getBuilding().getName());
        dto.setServiceID(meter.getService().getId());
        dto.setServiceName(meter.getService().getName());
        dto.setServiceUnitName(meter.getService().getUnit().getName());
        dto.setSection(meter.getSection());
        dto.setStatus(meter.getStatus().getName());
        dto.setDate(meter.getDate());
        dto.setApartmentOwnerID(meter.getApartment().getOwner().getId());
        dto.setApartmentOwnerFullName(meter.getApartment().getOwner().getFullName());

        return dto;
    }

}
