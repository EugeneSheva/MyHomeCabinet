package com.example.myhome.home.repos;

import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.model.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {

    @Query("SELECT sec FROM Building b JOIN b.sections sec WHERE b.id=?1")
    List<String> getBuildingSections(long building_id);

    @Query("SELECT DISTINCT a FROM Building b " +
            "JOIN b.sections sec " +
            "JOIN Apartment a ON a.building.id=b.id " +
            "WHERE a.building.id= ?1 " +
            "AND a.section= ?2")
    List<Apartment> getSectionApartments(long param_building_id, String section_name);

}
