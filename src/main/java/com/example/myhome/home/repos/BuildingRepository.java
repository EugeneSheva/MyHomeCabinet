package com.example.myhome.home.repos;

import com.example.myhome.home.model.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
}
