package com.example.myhome.home.service;

import com.example.myhome.home.model.*;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.RepairRequestRepository;
import com.example.myhome.home.repository.specifications.RequestSpecifications;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log
public class RepairRequestService {

    @Autowired
    private RepairRequestRepository repairRequestRepository;

    @Autowired private ApartmentService apartmentService;
    @Autowired private OwnerService ownerService;
    @Autowired private AdminService adminService;

    public List<RepairRequest> findAllRequests() {return repairRequestRepository.findAll();}

    public List<RepairRequest> findAllBySpecification(FilterForm filters) {
        log.info("Filters found");
        log.info(filters.toString());
        Long id = filters.getId();
        String description = filters.getDescription();
        RepairMasterType masterType = (filters.getMaster_type() != null) ? RepairMasterType.valueOf(filters.getMaster_type()) : null;
        String phone = filters.getPhone();
        RepairStatus status = (filters.getStatus() != null) ? RepairStatus.valueOf(filters.getStatus()) : null;
        Apartment apartment = (filters.getApartment() != null) ? apartmentService.findByNumber(filters.getApartment()) : null;
        Owner owner = (filters.getOwner() != null) ? ownerService.findById(filters.getOwner()) : null;
        Admin master = (filters.getMaster() != null) ? adminService.findAdminById(filters.getMaster()) : null;

        Specification<RepairRequest> specification =
                Specification.where(RequestSpecifications.hasId(id)
                                .and(RequestSpecifications.hasMasterType(masterType))
                                .and(RequestSpecifications.hasDescriptionLike(description))
                                .and(RequestSpecifications.hasApartment(apartment))
                                .and(RequestSpecifications.hasOwner(owner))
                                .and(RequestSpecifications.hasPhoneLike(phone))
                                .and(RequestSpecifications.hasMaster(master))
                                .and(RequestSpecifications.hasStatus(status)));

        List<RepairRequest> foundItems = repairRequestRepository.findAll(specification);
        log.info("Found items: " + foundItems.toString());

        return foundItems;
    }

    public RepairRequest findRequestById(long request_id) {return repairRequestRepository.findById(request_id).orElseThrow();}

    public RepairRequest saveRequest(RepairRequest request) {return repairRequestRepository.save(request);}

    public void deleteRequestById(long request_id) {repairRequestRepository.deleteById(request_id);}

}
