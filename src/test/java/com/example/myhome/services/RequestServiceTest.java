package com.example.myhome.services;

import com.example.myhome.home.dto.RepairRequestDTO;
import com.example.myhome.home.mapper.RepairRequestDTOMapper;
import com.example.myhome.home.model.RepairRequest;
import com.example.myhome.home.repository.RepairRequestRepository;
import com.example.myhome.home.service.impl.RepairRequestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RequestServiceTest {

    private RepairRequestServiceImpl repairRequestService;
    private RepairRequestRepository repairRequestRepository;
    private RepairRequestDTOMapper mapper;

    @BeforeEach
    void setUp() {
        repairRequestRepository = mock(RepairRequestRepository.class);
        mapper = mock(RepairRequestDTOMapper.class);
        repairRequestService = new RepairRequestServiceImpl(repairRequestRepository, mapper);
    }

    @Test
    void testFindReqoestDtoByOwnerId() {
        // Mock data
        Long ownerId = 1L;
        PageRequest pageable = PageRequest.of(0, 10);

        RepairRequest repairRequest1 = new RepairRequest();
        repairRequest1.setId(1L);
        RepairRequest repairRequest2 = new RepairRequest();
        repairRequest2.setId(2L);

        List<RepairRequest> repairRequestList = Arrays.asList(repairRequest1, repairRequest2);

        RepairRequestDTO dto1 = new RepairRequestDTO();
        dto1.setId(1L);
        RepairRequestDTO dto2 = new RepairRequestDTO();
        dto2.setId(2L);

        List<RepairRequestDTO> expectedDTOList = Arrays.asList(dto1, dto2);

        when(repairRequestRepository.findAllByOwnerId(ownerId, pageable))
                .thenReturn(new PageImpl<>(repairRequestList, pageable, repairRequestList.size()));
        when(mapper.fromRequestToDTO(repairRequest1)).thenReturn(dto1);
        when(mapper.fromRequestToDTO(repairRequest2)).thenReturn(dto2);


        Page<RepairRequestDTO> result = repairRequestService.findReqoestDtoByOwnerId(ownerId, pageable);


        assertEquals(expectedDTOList, result.getContent());
        assertEquals(2, result.getTotalElements());
    }
}


