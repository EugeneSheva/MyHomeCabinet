package com.example.myhome.home.service;

import com.example.myhome.home.dto.MeterDataDTO;
import com.example.myhome.home.model.MeterData;
import com.example.myhome.home.model.filter.FilterForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface MeterDataService {

    List<MeterData> findAllMeters();
    List<MeterData> findSingleMeterData(Long apartment_id, Long service_id);
    Page<MeterDataDTO> findSingleMeterData(FilterForm form, Pageable pageable);
    Page<MeterDataDTO> findAllByFiltersAndPage(FilterForm filters, Pageable pageable);

    MeterData findMeterDataById(Long meter_id);

    Long getMaxId();

    MeterData saveMeterData(MeterData meterData);

    void deleteMeterById(Long meter_id);

    Specification<MeterData> buildSpecFromFilters(FilterForm filterForm);

}
