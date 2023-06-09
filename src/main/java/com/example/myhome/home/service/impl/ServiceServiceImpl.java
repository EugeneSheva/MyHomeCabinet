package com.example.myhome.home.service.impl;


import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.model.Unit;
import com.example.myhome.home.repository.ServiceRepository;
import com.example.myhome.home.repository.UnitRepository;
import com.example.myhome.home.service.ServiceService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.myhome.home.model.Service;


import java.util.Arrays;
import java.util.List;

@org.springframework.stereotype.Service
@Log
public class ServiceServiceImpl implements ServiceService {

    @Autowired private ServiceRepository serviceRepository;
    @Autowired private UnitRepository unitRepository;

    @Override
    public List<Service> findAllServices() {return serviceRepository.findAll();}
    @Override
    public List<Unit> findAllUnits() {return unitRepository.findAll();}
    @Override
    public Service findServiceById(Long service_id) {return serviceRepository.findById(service_id).orElse(null);}
    @Override
    public String getServiceNameById(Long service_id) {return serviceRepository.findById(service_id).orElseThrow(NotFoundException::new).getName();}
    @Override
    public String getUnitNameById(Long unit_id) {return unitRepository.findById(unit_id).orElseThrow(NotFoundException::new).getName();}
    @Override
    public Service saveService(Service service) {return serviceRepository.save(service);}
    @Override
    public void deleteServiceById(Long service_id) {serviceRepository.deleteById(service_id);}
    @Override
    public void deleteUnitById(Long unit_id) {unitRepository.deleteById(unit_id);}
    @Override
    public List<Unit> addNewUnits(List<Unit> unitList, String[] new_unit_names) {
        log.info(unitList.toString());
        log.info(Arrays.toString(new_unit_names));

        if(new_unit_names != null) {

            log.info(Arrays.toString(new_unit_names));

            for (int i = 0; i < new_unit_names.length-1; i++) {
                log.info("creating new unit");
                Unit unit = new Unit();
                unit.setName(new_unit_names[i]);
                unitList.add(unit);
            }
        }

        log.info(unitList.toString());

        return unitRepository.saveAll(unitList);
    }
    @Override
    public List<Service> addNewServices(List<Service> serviceList,
                                        String[] new_service_names,
                                        String[] new_service_unit_names,
                                        String[] new_service_show_in_meters) {

        log.info(serviceList.toString());

        for (int i = 0; i < new_service_names.length-1; i++) {
            if(new_service_names[i].equalsIgnoreCase("") || new_service_names[i] == null) continue;
            Service service = new Service();
            service.setName(new_service_names[i]);
            service.setShow_in_meters(true);
            service.setUnit(unitRepository.findByName(new_service_unit_names[i]).orElseGet(Unit::new));
            serviceList.add(service);
        }

        return serviceRepository.saveAll(serviceList);
    }
}
