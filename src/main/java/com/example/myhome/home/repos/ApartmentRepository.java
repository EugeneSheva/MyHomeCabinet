package com.example.myhome.home.repos;

import com.example.myhome.home.model.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

//    List<Apartment> getSectionApartments(long building_id, String section_name);

    Long getNumberById(long flat_id);

}
