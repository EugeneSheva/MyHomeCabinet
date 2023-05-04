package com.example.myhome.home.service;

import com.example.myhome.home.dto.RepairRequestDTO;
import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.mapper.RepairRequestDTOMapper;
import com.example.myhome.home.model.*;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.AdminRepository;
import com.example.myhome.home.repository.ApartmentRepository;
import com.example.myhome.home.repository.OwnerRepository;
import com.example.myhome.home.repository.RepairRequestRepository;
import com.example.myhome.home.service.impl.AdminServiceImpl;
import com.example.myhome.home.specification.RequestSpecifications;
import com.example.myhome.util.MappingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log
public class RepairRequestService {

    private final RepairRequestRepository repairRequestRepository;

    private final ApartmentService apartmentService;
    private final OwnerService ownerService;
    private final AdminServiceImpl adminService;

    private final ApartmentRepository apartmentRepository;
    private final OwnerRepository ownerRepository;
    private final AdminRepository adminRepository;

    private final RepairRequestDTOMapper mapper;

    public List<RepairRequest> findAllRequests() {
        log.info("Searching for requests");
        List<RepairRequest> list = repairRequestRepository.findAll();
        log.info("Found " + list.size() + " requests");
        return list;
    }

    public Page<RepairRequest> findAllBySpecification(FilterForm filters) throws IllegalAccessException {
        log.info("Searching for requests using filters: " + filters.toString());
        Page<RepairRequest> page =
                (filters.filtersPresent()) ?
                        repairRequestRepository.findAll(buildSpecFromFilters(filters), PageRequest.of(filters.getPage()-1,10)) :
                        repairRequestRepository.findAll(PageRequest.of(filters.getPage()-1, 10));
        log.info("Found " + page.getTotalElements() + " requests, total pages " + page.getTotalPages());
        return page;
    }

    public Page<RepairRequestDTO> findAllBySpecification(FilterForm filters, Integer page, Integer size) throws IllegalAccessException {

        Pageable pageable = PageRequest.of(page-1,size);

        Page<RepairRequest> initialPage = repairRequestRepository.findAll(buildSpecFromFilters(filters), pageable);

        List<RepairRequestDTO> listDTO = initialPage.getContent().stream()
                .map(mapper::fromRequestToDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(listDTO, pageable, initialPage.getTotalElements());
    }

    public Specification<RepairRequest> buildSpecFromFilters(FilterForm filters) {

        log.info("Building specification from filters");

        Long id = filters.getId();
        String description = filters.getDescription();
        RepairMasterType masterType = (filters.getMaster_type() != null) ? RepairMasterType.valueOf(filters.getMaster_type()) : null;
        String phone = filters.getPhone();
        RepairStatus status = (filters.getStatus() != null) ? RepairStatus.valueOf(filters.getStatus()) : null;
        Apartment apartment = (filters.getApartment() != null) ? apartmentService.findByNumber(filters.getApartment()) : null;
        Owner owner = (filters.getOwner() != null) ? ownerService.findById(filters.getOwner()) : null;
        Admin master = (filters.getMaster() != null) ? adminService.findAdminById(filters.getMaster()) : null;

        String datetime = filters.getDatetime();
        LocalDateTime from = null, to = null;

        if(datetime != null && !datetime.isEmpty()) {
            String datetime_from = datetime.split(" to ")[0];
            from =
                    LocalDateTime.of(LocalDate.parse(datetime_from.split(" ")[0]),
                            LocalTime.parse(datetime_from.split(" ")[1]));
            String datetime_to = datetime.split(" to ")[1];
            to =
                    LocalDateTime.of(LocalDate.parse(datetime_to.split(" ")[0]),
                            LocalTime.parse(datetime_to.split(" ")[1]));
        }

        Specification<RepairRequest> spec = Specification.where(RequestSpecifications.hasId(id)
                .and(RequestSpecifications.hasMasterType(masterType))
                .and(RequestSpecifications.hasDescriptionLike(description))
                .and(RequestSpecifications.hasApartment(apartment))
                .and(RequestSpecifications.hasOwner(owner))
                .and(RequestSpecifications.hasPhoneLike(phone))
                .and(RequestSpecifications.hasMaster(master))
                .and(RequestSpecifications.hasStatus(status))
                .and(RequestSpecifications.datesBetween(from, to)));

        log.info("Final specification for use: " + spec);

        return spec;
    }

    public Long getMaxId() {return repairRequestRepository.getMaxId().orElse(0L);}

    public RepairRequest findRequestById(long request_id) {
        log.info("Looking for request with ID: " + request_id);
        try {
            RepairRequest req = repairRequestRepository.findById(request_id).orElseThrow(NotFoundException::new);
            log.info("Request found! " + req);
            return req;
        } catch (NotFoundException e) {
            log.warning("Request with ID " + request_id + " not found!");
            log.warning(e.getMessage());
            return null;
        }
    }

    public RepairRequestDTO findRequestDTOById(Long request_id) {
        RepairRequest request = findRequestById(request_id);
        if(request != null) return mapper.fromRequestToDTO(request);
        else return null;
    }

    public RepairRequest saveRequest(RepairRequest request) {
        log.info("Trying to save request...");
        log.info(request.toString());
        RepairRequest savedRequest;
        try {
            savedRequest = repairRequestRepository.save(request);
            log.info("Request successfully saved! Saved request: ");
            log.info(savedRequest.toString());
            return savedRequest;
        } catch (Exception e) {
            log.severe("Request couldn't be saved");
            log.severe(e.getMessage());
            return null;
        }
    }

    public RepairRequest saveRequest(RepairRequestDTO dto) {

        log.info("Forming repair request to save from DTO");
        RepairRequest request = mapper.fromDTOToRequest(dto);
        request.setApartment(apartmentRepository.getReferenceById(dto.getApartmentID()));
        request.setOwner(ownerRepository.getReferenceById(dto.getOwnerID()));
        request.setMaster(adminRepository.getReferenceById(dto.getMasterID()));
        log.info("Request formation finished, trying to save...");
        log.info(request.toString());

        try {
            RepairRequest savedRequest = repairRequestRepository.save(request);
            log.info("Request successfully saved! Saved request: ");
            log.info(savedRequest.toString());
            return savedRequest;
        } catch (Exception e) {
            log.severe("Request couldn't be saved");
            log.severe(e.getMessage());
            return null;
        }
    }

    public void deleteRequestById(long request_id) {
        log.info("Deleting request with ID: " + request_id);
        try {
            repairRequestRepository.deleteById(request_id);
        } catch (Exception e) {
            log.severe("Deletion failed!");
            log.severe(e.getCause().getMessage());
        }
        log.info("Request with ID " + request_id + " successfully deleted");
    }

    public Page<RepairRequestDTO> findAll(Pageable pageable){
        List<RepairRequestDTO>listDTO= new ArrayList<>();
        Page<RepairRequest>repairRequests = repairRequestRepository.findAll(pageable);

        repairRequests.forEach(req -> listDTO.add(mapper.fromRequestToDTO(req)));

        System.out.println("service "+repairRequests.getTotalElements());

        return new PageImpl<>(listDTO, pageable, repairRequests.getTotalElements());
    }



}
