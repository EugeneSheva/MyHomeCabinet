package com.example.myhome.home.mapper;

import com.example.myhome.home.dto.RepairRequestDTO;
import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.model.RepairMasterType;
import com.example.myhome.home.model.RepairRequest;
import com.example.myhome.home.repository.AdminRepository;
import com.example.myhome.home.repository.ApartmentRepository;
import com.example.myhome.home.repository.BuildingRepository;
import com.example.myhome.home.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class RepairRequestDTOMapper {

//    private final ApartmentRepository apartmentRepository;
//    private final OwnerRepository ownerRepository;
//    private final AdminRepository adminRepository;

    public RepairRequest fromDTOToRequest(RepairRequestDTO dto) {
        if(dto == null) return null;

        RepairRequest request = new RepairRequest();
        request.setId(dto.getId());
        request.setBest_time_request(LocalDateTime.parse(dto.getBest_time()));
        request.setMaster_type(RepairMasterType.valueOf(dto.getMaster_type()));
        request.setDescription(dto.getDescription());

//        request.setApartment(apartmentRepository.getReferenceById(dto.getApartmentID()));
//        request.setOwner(ownerRepository.getReferenceById(dto.getOwnerID()));
//        request.setMaster(adminRepository.getReferenceById(dto.getMasterID()));

        return request;
    }
    public RepairRequestDTO fromRequestToDTO(RepairRequest request) {
        if(request == null) return null;

        Long apartmentID = (request.getApartment() != null) ? request.getApartment().getId() : null;
        Long apartmentNumber = (request.getApartment() != null) ? request.getApartment().getNumber() : null;
        String apartmentBuildingName = (request.getApartment() != null) ? request.getApartment().getBuilding().getName() : null;
        Long ownerID = (request.getApartment().getOwner() != null) ? request.getApartment().getOwner().getId() : null;
        String ownerFullName = (request.getApartment().getOwner() != null) ? request.getApartment().getOwner().getFullName() : null;
        String ownerPhoneNumber = (request.getApartment().getOwner() != null) ? request.getApartment().getOwner().getPhone_number() : null;
        Long masterID = (request.getMaster() != null) ? request.getMaster().getId() : null;
        String masterFullName = (request.getMaster() != null) ? request.getMaster().getFullName() : null;

        return RepairRequestDTO.builder()
                .id(request.getId())
                .best_time(request.getBest_time_request().format(DateTimeFormatter.ofPattern("yyyy-MM-dd - HH:mm")))
                .master_type(request.getMaster_type().getName())
                .description(request.getDescription())
                .request_date(request.getRequest_date().toLocalDate())
                .request_time(request.getRequest_date().toLocalTime())
                .apartmentID(apartmentID)
                .apartmentNumber(apartmentNumber)
                .apartmentBuildingName(apartmentBuildingName)
                .ownerID(ownerID)
                .ownerFullName(ownerFullName)
                .ownerPhoneNumber(ownerPhoneNumber)
                .masterID(masterID)
                .masterFullName(masterFullName)
                .status(request.getStatus())
                .statusName(request.getStatus().getName())
                .build();
    }

}
