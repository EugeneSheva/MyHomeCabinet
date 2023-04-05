package com.example.myhome.home.service;

import com.example.myhome.home.model.MeterData;
import com.example.myhome.home.repository.MeterDataRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log
public class MeterDataService {

    @Autowired
    private MeterDataRepository meterDataRepository;

    public List<MeterData> findAllMeters() {return meterDataRepository.findAll();}
    public List<MeterData> findAllMetersById(List<Long> ids) {return meterDataRepository.findAllById(ids);}
    public List<MeterData> findSingleMeterData(Long apartment_id, Long service_id) {
        if(service_id != null) return meterDataRepository.findSingleMeterData(apartment_id, service_id);
        else return meterDataRepository.findByApartmentId(apartment_id);
    }

    public Optional<MeterData> findFirstByOrderByIdDesc() {return meterDataRepository.findFirstByOrderByIdDesc();}

    public List<Long> findMeterIds() {return meterDataRepository.findDistinctGroupByApartmentAndService();}

    public MeterData findMeterData(long meter_id) {return meterDataRepository.findById(meter_id).orElseThrow();}

    public MeterData saveMeterData(MeterData meterData) {return meterDataRepository.save(meterData);}

    public void deleteMeter(long meter_id) {meterDataRepository.deleteById(meter_id);}

    public List<MeterData> filter(List<MeterData> meterDataList, Long building, String section, Long apartment, Long service) {
        if(building != null) meterDataList = meterDataList.stream()
                    .filter(meter -> Objects.equals(meter.getApartment().getBuilding().getId(), building))
                    .collect(Collectors.toList());
        if(apartment != null) meterDataList = meterDataList.stream()
                .filter(meter -> Objects.equals(meter.getApartment().getNumber(), apartment))
                .collect(Collectors.toList());
        if(service != null) meterDataList = meterDataList.stream()
                .filter(meter -> Objects.equals(meter.getService().getId(), service))
                .collect(Collectors.toList());
        if(section != null) meterDataList = meterDataList.stream()
                .filter(meter -> Objects.equals(meter.getApartment().getSection(), section))
                .collect(Collectors.toList());
        log.info(meterDataList.toString());

        return meterDataList;
    }

}
