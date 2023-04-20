package com.example.myhome.home.repository;

import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.model.Building;
import com.example.myhome.home.model.BuildingDTO;
import com.example.myhome.home.model.RepairStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.myhome.home.specification.BuildingSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BuildingRepository extends JpaRepository<Building, Long>, JpaSpecificationExecutor<Building> {

//      @Query("SELECT new com.example.myhome.home.model.BuildingDTO(building.id, building.name,  building.address) FROM Building building")
//      List<BuildingDTO>getBuildingsDTOselect();
//        List<BuildingDTO> findAllProjectedBy();

        Long countAllBy();

        @Query(value="SELECT b FROM Building b WHERE b.name LIKE %:name%")
        Page<Building> findByName(String name, Pageable pageable);

        @Query("SELECT sec FROM Building b JOIN b.sections sec WHERE b.id=?1")
        List<String> getBuildingSections(long building_id);

        @Query("SELECT DISTINCT a FROM Building b " +
                "JOIN b.sections sec " +
                "JOIN Apartment a ON a.building.id=b.id " +
                "WHERE a.building.id= ?1 " +
                "AND a.section= ?2")
        List<Apartment> getSectionApartments(long param_building_id, String section_name);

        default Page<Building> findByFilters(String name, String address, Pageable pageable) {
                Specification<Building> spec = Specification.where(null);

                if (name != null && !name.isEmpty()) {
                        spec = spec.and(BuildingSpecification.nameContains(name));
                }

                if (address != null && !address.isEmpty()) {
                        spec = spec.and(BuildingSpecification.addressContains(address));
                }
                return findAll(spec,pageable);
        }

        Building findByName(String name);

        Page<Building> findAll(Pageable pageable);
}
