package com.example.myhome.home.service;

import com.example.myhome.home.dto.RepairRequestDTO;
import com.example.myhome.home.model.RepairRequest;
import com.example.myhome.home.model.filter.FilterForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface RepairRequestService {
    List<RepairRequest> findAllRequests();

    Page<RepairRequest> findAllBySpecification(FilterForm filters) throws IllegalAccessException;

    Page<RepairRequestDTO> findAllBySpecification(FilterForm filters, Integer page, Integer size) throws IllegalAccessException;

    Specification<RepairRequest> buildSpecFromFilters(FilterForm filters);

    Long getMaxId();

    RepairRequest findRequestById(long request_id);

    RepairRequestDTO findRequestDTOById(Long request_id);

    RepairRequest saveRequest(RepairRequest request);

    RepairRequest saveRequest(RepairRequestDTO dto);

    void deleteRequestById(long request_id);

    Page<RepairRequestDTO> findAll(Pageable pageable);

    Page<RepairRequestDTO> findReqoestDtoByOwnerId(Long id, Pageable pageable);
}
