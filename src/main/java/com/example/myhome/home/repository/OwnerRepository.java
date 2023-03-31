package com.example.myhome.home.repository;

import com.example.myhome.home.model.Building;
import com.example.myhome.home.model.BuildingDTO;
import com.example.myhome.home.model.Owner;
import com.example.myhome.home.model.OwnerDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

//    List<OwnerDTO> findAllProjectedBy();
}
