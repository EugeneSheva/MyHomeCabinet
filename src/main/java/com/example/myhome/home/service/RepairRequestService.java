package com.example.myhome.home.service;

import com.example.myhome.home.model.RepairRequest;
import com.example.myhome.home.repository.RepairRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepairRequestService {

    @Autowired
    private RepairRequestRepository repairRequestRepository;

    public List<RepairRequest> findAllRequests() {return repairRequestRepository.findAll();}

    public RepairRequest findRequestById(long request_id) {return repairRequestRepository.findById(request_id).orElseThrow();}

    public RepairRequest saveRequest(RepairRequest request) {return repairRequestRepository.save(request);}

    public void deleteRequestById(long request_id) {repairRequestRepository.deleteById(request_id);}

}
