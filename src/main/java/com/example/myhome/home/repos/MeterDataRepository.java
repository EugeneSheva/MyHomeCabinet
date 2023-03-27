package com.example.myhome.home.repos;

import com.example.myhome.home.model.MeterData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeterDataRepository extends JpaRepository<MeterData, Long> {

    Optional<MeterData> findFirstByOrderByIdDesc();

}
