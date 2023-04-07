package com.example.myhome.home.service;

import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.model.Building;
import com.example.myhome.home.model.MeterData;
import com.example.myhome.home.model.MeterPaymentStatus;
import com.example.myhome.home.repository.MeterDataRepository;
import com.example.myhome.home.repository.specifications.MeterSpecifications;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log
public class MeterDataService {

    @Autowired
    private MeterDataRepository meterDataRepository;

    @Autowired private ApartmentService apartmentService;
    @Autowired private ServiceService serviceService;
    @Autowired private BuildingService buildingService;

    public List<MeterData> findAllMeters() {return meterDataRepository.findAll();}
    public List<MeterData> findAllMetersById(List<Long> ids) {return meterDataRepository.findAllById(ids);}
    public List<MeterData> findSingleMeterData(Long apartment_id, Long service_id) {
        if(service_id != null) return meterDataRepository.findSingleMeterData(apartment_id, service_id);
        else return meterDataRepository.findByApartmentId(apartment_id);
    }

    public List<MeterData> findAllBySpecification(Long building_id, String section_name, Long apartment_number, Long service_id) {
        Building building = (building_id != null) ? buildingService.findById(building_id) : null;
        Apartment apartment = (apartment_number != null) ? apartmentService.findByNumber(apartment_number) : null;
        com.example.myhome.home.model.Service service = (service_id != null) ? serviceService.findServiceById(service_id) : null;

        log.info("Building: " + building);
        log.info("Apartment: " + apartment);
        log.info("Service: " + service);
        log.info("Section: " + section_name);

        return meterDataRepository.findAll(MeterSpecifications.hasBuilding(building)
                                        .and(MeterSpecifications.hasSection(section_name))
                                        .and(MeterSpecifications.hasApartment(apartment))
                                        .and(MeterSpecifications.hasService(service)));
    }

    public Long getMaxIdPlusOne() {return meterDataRepository.getMaxId().orElse(0L)+1L;}

    public Optional<MeterData> findFirstByOrderByIdDesc() {return meterDataRepository.findFirstByOrderByIdDesc();}

    public List<Long> findMeterIds() {return meterDataRepository.findDistinctGroupByApartmentAndService();}

    public MeterData findMeterData(long meter_id) {return meterDataRepository.findById(meter_id).orElseThrow();}

    public MeterData saveMeterData(MeterData meterData) {return meterDataRepository.save(meterData);}
    public MeterData saveMeterDataAJAX(Long initial_meter_id,    // <-- если хочешь обновить существующий
                                       String apartment_id,
                                       String readings,
                                       String stat,
                                       String service_id,
                                       String date) {

        MeterData newMeter = (initial_meter_id != null) ? findMeterData(initial_meter_id) : new MeterData();
        newMeter.setApartment(apartmentService.findById(Long.parseLong(apartment_id)));
        newMeter.setCurrentReadings(Double.parseDouble(readings));
        newMeter.setStatus(MeterPaymentStatus.valueOf(stat));
        newMeter.setService(serviceService.findServiceById(Long.parseLong(service_id)));
        newMeter.setDate(LocalDate.parse(date));

        return saveMeterData(newMeter);
    }

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
