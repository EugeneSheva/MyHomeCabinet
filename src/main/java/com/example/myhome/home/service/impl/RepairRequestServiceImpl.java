package com.example.myhome.home.service.impl;

import com.example.myhome.home.dto.RepairRequestDTO;
import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.mapper.RepairRequestDTOMapper;
import com.example.myhome.home.model.*;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.*;
import com.example.myhome.home.service.OwnerService;
import com.example.myhome.home.service.RepairRequestService;
import com.example.myhome.home.service.impl.AdminServiceImpl;
import com.example.myhome.home.service.impl.ApartmentServiceImpl;
import com.example.myhome.home.specification.RequestSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log
public class RepairRequestServiceImpl implements RepairRequestService {

    private final RepairRequestRepository repairRequestRepository;

    private final ApartmentServiceImpl apartmentService;
    private final OwnerService ownerService;
    private final AdminServiceImpl adminService;

    private final ApartmentRepository apartmentRepository;
    private final OwnerRepository ownerRepository;
    private final AdminRepository adminRepository;
    private final UserRoleRepository userRoleRepository;

    private final RepairRequestDTOMapper mapper;




//    @Override
//    public RepairRequest findRequestById(long request_id) {
//        log.info("Looking for request with ID: " + request_id);
//        try {
//            RepairRequest req = repairRequestRepository.findById(request_id).orElseThrow(NotFoundException::new);
//            log.info("Request found! " + req);
//            return req;
//        } catch (NotFoundException e) {
//            log.warning("Request with ID " + request_id + " not found!");
//            log.warning(e.getMessage());
//            return null;
//        }
//    }

//    @Override
//    public RepairRequestDTO findRequestDTOById(Long request_id) {
//        RepairRequest request = findRequestById(request_id);
//        if(request != null) return mapper.fromRequestToDTO(request);
//        else return null;
//    }

//    @Override
//    public RepairRequest saveRequest(RepairRequest request) {
//        log.info("Trying to save request...");
//        log.info(request.toString());
//        RepairRequest savedRequest;
//        try {
//            savedRequest = repairRequestRepository.save(request);
//            log.info("Request successfully saved! Saved request: ");
//            log.info(savedRequest.toString());
//            return savedRequest;
//        } catch (Exception e) {
//            log.severe("Request couldn't be saved");
//            log.severe(e.getMessage());
//            return null;
//        }
//    }

//    @Override
//    public RepairRequest saveRequest(RepairRequestDTO dto) {
//
//        log.info("Forming repair request to save from DTO");
//        RepairRequest request = mapper.fromDTOToRequest(dto);
//        request.setApartment(apartmentRepository.getReferenceById(dto.getApartmentID()));
//        request.setOwner(ownerRepository.getReferenceById(dto.getOwnerID()));
//        request.setMaster(adminRepository.getReferenceById(dto.getMasterID()));
//        log.info("Request formation finished, trying to save...");
//        log.info(request.toString());
//
//        try {
//            RepairRequest savedRequest = repairRequestRepository.save(request);
//            log.info("Request successfully saved! Saved request: ");
//            log.info(savedRequest.toString());
//            return savedRequest;
//        } catch (Exception e) {
//            log.severe("Request couldn't be saved");
//            log.severe(e.getMessage());
//            return null;
//        }
//    }

//    @Override
//    public void deleteRequestById(long request_id) {
//        log.info("Deleting request with ID: " + request_id);
//        try {
//            repairRequestRepository.deleteById(request_id);
//        } catch (Exception e) {
//            log.severe("Deletion failed!");
//            log.severe(e.getCause().getMessage());
//        }
//        log.info("Request with ID " + request_id + " successfully deleted");
//    }

//    @Override
//    public Page<RepairRequestDTO> findAll(Pageable pageable){
//        List<RepairRequestDTO>listDTO= new ArrayList<>();
//        Page<RepairRequest>repairRequests = repairRequestRepository.findAll(pageable);
//
//        repairRequests.forEach(req -> listDTO.add(mapper.fromRequestToDTO(req)));
//
//        System.out.println("service "+repairRequests.getTotalElements());
//
//        return new PageImpl<>(listDTO, pageable, repairRequests.getTotalElements());
//    }
    @Override
    public Page<RepairRequestDTO> findReqoestDtoByOwnerId(Long id, Pageable pageable) {
        Page<RepairRequest>repairRequestPage=repairRequestRepository.findAllByOwnerId(id, pageable);
        List<RepairRequestDTO> repairRequestDTOList = new ArrayList<>();

        for (RepairRequest repairRequest : repairRequestPage.getContent()) {
            RepairRequestDTO repairRequestDTO = mapper.fromRequestToDTO(repairRequest);
            repairRequestDTOList.add(repairRequestDTO);
        }
        return new PageImpl<>(repairRequestDTOList, pageable, repairRequestPage.getTotalElements());
    }




}
