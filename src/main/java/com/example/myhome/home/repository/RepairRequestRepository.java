package com.example.myhome.home.repository;

import com.example.myhome.home.model.RepairRequest;
import com.example.myhome.home.model.RepairStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairRequestRepository extends JpaRepository<RepairRequest, Long> {
    Long countRepairRequestsByStatus(RepairStatus status);
}
