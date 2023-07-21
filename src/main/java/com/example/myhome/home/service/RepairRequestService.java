package com.example.myhome.home.service;

import com.example.myhome.home.dto.RepairRequestDTO;
import com.example.myhome.home.model.RepairRequest;
import com.example.myhome.home.model.filter.FilterForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface RepairRequestService {
        Page<RepairRequestDTO> findReqoestDtoByOwnerId(Long id, Pageable pageable);
}
