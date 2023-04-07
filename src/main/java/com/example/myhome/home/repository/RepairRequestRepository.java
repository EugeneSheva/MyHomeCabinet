package com.example.myhome.home.repository;

import com.example.myhome.home.model.RepairRequest;
import com.example.myhome.home.model.RepairStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairRequestRepository extends JpaRepository<RepairRequest, Long>, JpaSpecificationExecutor<RepairRequest> {
    Long countRepairRequestsByStatus(RepairStatus status);
}
