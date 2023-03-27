package com.example.myhome.home.repos;

import com.example.myhome.home.model.BuildingFloor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingFloorsRepository extends JpaRepository<BuildingFloor, Long> {
}
