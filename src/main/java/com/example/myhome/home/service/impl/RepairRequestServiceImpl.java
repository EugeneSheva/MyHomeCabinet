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
    private final RepairRequestDTOMapper mapper;

    @Override
    public Page<RepairRequestDTO> findReqoestDtoByOwnerId(Long id, Pageable pageable) {
        Page<RepairRequest>repairRequestPage=repairRequestRepository.findAllByOwnerId(id, pageable);
        List<RepairRequestDTO> repairRequestDTOList = new ArrayList<>();

        System.out.println("repairRequestPage " + repairRequestPage);
        System.out.println("repairRequestPage.getContent() " + repairRequestPage.getContent());

        for (RepairRequest repairRequest : repairRequestPage.getContent()) {
            System.out.println("repairRequest " + repairRequest);
            RepairRequestDTO repairRequestDTO = mapper.fromRequestToDTO(repairRequest);
            repairRequestDTOList.add(repairRequestDTO);
        }
        return new PageImpl<>(repairRequestDTOList, pageable, repairRequestPage.getTotalElements());
    }


}
