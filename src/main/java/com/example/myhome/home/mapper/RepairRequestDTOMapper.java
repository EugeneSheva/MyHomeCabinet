package com.example.myhome.home.mapper;

import com.example.myhome.home.dto.RepairRequestDTO;
import com.example.myhome.home.model.*;
import com.example.myhome.home.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class RepairRequestDTOMapper {

    @Autowired
    private  UserRoleRepository repository;
    @Autowired
    private  MessageSource messageSource;

    public RepairRequest fromDTOToRequest(RepairRequestDTO dto) {
        if(dto == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd - HH:mm");
        RepairRequest request = new RepairRequest();
        UserRole masterType = (dto.getMasterTypeID() != null && dto.getMasterTypeID() > 0) ? repository.getReferenceById(dto.getMasterTypeID()) : null;
        request.setId(dto.getId());
        request.setBest_time_request(LocalDateTime.parse(dto.getBest_time(), formatter));
        request.setRequest_date(LocalDateTime.of(dto.getRequest_date(), dto.getRequest_time()));
        request.setStatus(dto.getStatus());
        request.setMaster_type(masterType);
        request.setDescription(dto.getDescription());
        request.setComment(dto.getComment());

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
        Long masterTypeID = (request.getMaster_type() != null) ? request.getMaster_type().getId() : null;
        System.out.println("request. " + request);
        System.out.println("request.getMaster_type() " + request.getMaster_type());
        String masterTypeName = (request.getMaster_type() != null && request.getMaster_type().getName() != null) ? request.getMaster_type().getName() : messageSource.getMessage("any_specialist", null, LocaleContextHolder.getLocale());

        return RepairRequestDTO.builder()
                .id(request.getId())
                .best_time(request.getBest_time_request().format(DateTimeFormatter.ofPattern("yyyy-MM-dd - HH:mm")))
                .masterTypeID(masterTypeID)
                .masterTypeName(masterTypeName)
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
                .comment(request.getComment())
                .build();
    }

}
