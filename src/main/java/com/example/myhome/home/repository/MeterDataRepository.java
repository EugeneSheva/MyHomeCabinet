package com.example.myhome.home.repository;

import com.example.myhome.home.model.MeterData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeterDataRepository extends JpaRepository<MeterData, Long> {

    Optional<MeterData> findFirstByOrderByIdDesc();

    List<MeterData> findByApartmentId(long apartment_id);

    @Query("FROM MeterData md WHERE md.apartment.id=?1 AND md.service.id=?2")
    List<MeterData> findSingleMeterData(long apartment_id, long service_id);

    @Query("SELECT DISTINCT MAX(m.id) FROM MeterData m GROUP BY m.apartment, m.service")
    List<Long> findDistinctGroupByApartmentAndService();

}
