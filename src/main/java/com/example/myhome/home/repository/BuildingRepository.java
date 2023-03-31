package com.example.myhome.home.repository;

import com.example.myhome.home.model.Building;
import com.example.myhome.home.model.BuildingDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BuildingRepository extends JpaRepository<Building, Long> {

//      @Query("SELECT new com.example.myhome.home.model.BuildingDTO(building.id, building.name,  building.address) FROM Building building")
//      List<BuildingDTO>getBuildingsDTOselect();
        List<BuildingDTO> findAllProjectedBy();
}
