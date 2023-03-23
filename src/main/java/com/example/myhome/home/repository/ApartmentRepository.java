package com.example.myhome.home.repository;

import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.model.Building;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
}
