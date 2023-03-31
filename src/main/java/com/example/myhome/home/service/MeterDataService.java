package com.example.myhome.home.service;

import com.example.myhome.home.model.MeterData;
import com.example.myhome.home.repository.MeterDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MeterDataService {

    @Autowired
    private MeterDataRepository meterDataRepository;

    public List<MeterData> findAllMeters() {return meterDataRepository.findAll();}
    public List<MeterData> findAllMetersById(List<Long> ids) {return meterDataRepository.findAllById(ids);}
    public List<MeterData> findSingleMeterData(long apartment_id, long service_id) {return meterDataRepository.findSingleMeterData(apartment_id, service_id);}

    public Optional<MeterData> findFirstByOrderByIdDesc() {return meterDataRepository.findFirstByOrderByIdDesc();}

    public List<Long> findMeterIds() {return meterDataRepository.findDistinctGroupByApartmentAndService();}

    public MeterData findMeterData(long meter_id) {return meterDataRepository.findById(meter_id).orElseThrow();}

    public MeterData saveMeterData(MeterData meterData) {return meterDataRepository.save(meterData);}

    public void deleteMeter(long meter_id) {meterDataRepository.deleteById(meter_id);}

}
