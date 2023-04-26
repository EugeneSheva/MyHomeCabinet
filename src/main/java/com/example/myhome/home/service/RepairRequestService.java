package com.example.myhome.home.service;

import com.example.myhome.home.dto.RepairRequestDTO;
import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.model.*;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.RepairRequestRepository;
import com.example.myhome.home.specification.RequestSpecifications;
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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Log
public class RepairRequestService {

    @Autowired
    private RepairRequestRepository repairRequestRepository;

    @Autowired private ApartmentService apartmentService;
    @Autowired private OwnerService ownerService;
    @Autowired private AdminService adminService;

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
                .map(request -> {
                    Long apartmentID = (request.getApartment() != null) ? request.getApartment().getId() : null;
                    Long apartmentNumber = (request.getApartment() != null) ? request.getApartment().getNumber() : null;
                    String apartmentBuildingName = (request.getApartment() != null) ? request.getApartment().getBuilding().getName() : null;
                    Long ownerID = (request.getOwner() != null) ? request.getOwner().getId() : null;
                    String ownerFullName = (request.getOwner() != null) ? request.getOwner().getFullName() : null;
                    String ownerPhoneNumber = (request.getOwner() != null) ? request.getOwner().getPhone_number() : null;
                    Long masterID = (request.getMaster() != null) ? request.getMaster().getId() : null;
                    String masterFullName = (request.getMaster() != null) ? request.getMaster().getFullName() : null;

                    return RepairRequestDTO.builder()
                            .id(request.getId())
                            .best_time(request.getBest_time_request().format(DateTimeFormatter.ofPattern("yyyy-MM-dd - HH:mm")))
                            .master_type(request.getMaster_type().getName())
                            .description(request.getDescription())
                            .apartmentID(apartmentID)
                            .apartmentNumber(apartmentNumber)
                            .apartmentBuildingName(apartmentBuildingName)
                            .ownerID(ownerID)
                            .ownerFullName(ownerFullName)
                            .ownerPhoneNumber(ownerPhoneNumber)
                            .masterID(masterID)
                            .masterFullName(masterFullName)
                            .status(request.getStatus().getName())
                            .build();
                })
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

}
