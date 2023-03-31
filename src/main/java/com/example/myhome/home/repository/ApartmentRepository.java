package com.example.myhome.home.repository;

import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.model.ApartmentDTO;
import com.example.myhome.home.model.BuildingDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

    List<Apartment>findApartmentsByBuildingIdAndSection(Long id, String section);
    List<ApartmentDTO> findAllProjectedBy();

    List<Apartment>findApartmentsByBalanceBefore(Double balance);
    List<Apartment>findApartmentsByBuildingId(Long id);
    List<Apartment>findApartmentsByBuildingIdAndBalanceBefore(Long id, Double balance);

    List<Apartment>findApartmentsByBuildingIdAndSectionContainingIgnoreCase(Long id, String section);
    List<Apartment>findApartmentsByBuildingIdAndSectionContainingIgnoreCaseAndBalanceBefore(Long id, String section, Double balance);
    List<Apartment>findApartmentsByBuildingIdAndFloorContainingIgnoreCase(Long id, String floor);
    List<Apartment>findApartmentsByBuildingIdAndFloorContainingIgnoreCaseAndBalanceBefore(Long id, String floor, Double balance);
    List<Apartment>findApartmentsByBuildingIdAndSectionContainingIgnoreCaseAndFloorContainingIgnoreCase(Long id, String section, String floor);
    List<Apartment>findApartmentsByBuildingIdAndSectionContainingIgnoreCaseAndFloorContainingIgnoreCaseAndBalanceBefore(Long id, String section, String floor, Double balance);



}

