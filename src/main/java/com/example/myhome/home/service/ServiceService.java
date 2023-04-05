package com.example.myhome.home.service;


import com.example.myhome.home.model.Unit;
import com.example.myhome.home.repository.ServiceRepository;
import com.example.myhome.home.repository.UnitRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.myhome.home.model.Service;


import java.util.Arrays;
import java.util.List;

@org.springframework.stereotype.Service
@Log
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private UnitRepository unitRepository;

    public List<Service> findAllServices() {return serviceRepository.findAll();}

    public Service findServiceById(long service_id) {return serviceRepository.findById(service_id).orElseThrow();}
    public String getServiceNameById(long service_id) {return serviceRepository.findById(service_id).orElseThrow().getName();}

    public String getUnitNameById(long unit_id) {return unitRepository.findById(unit_id).orElseThrow().getName();}

    public Service saveService(Service service) {return serviceRepository.save(service);}

    public void deleteServiceById(long service_id) {serviceRepository.deleteById(service_id);}

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

    public List<Service> addNewServices(List<Service> serviceList,
                                        String[] new_service_names,
                                        String[] new_service_unit_names,
                                        String[] new_service_show_in_meters) {

        log.info(serviceList.toString());

        for (int i = 0; i < new_service_names.length-1; i++) {
            Service service = new Service();
            service.setName(new_service_names[i]);
            //service.setShow_in_meters(Boolean.parseBoolean(new_service_show_in_meters[i]));
            service.setUnit(unitRepository.findByName(new_service_unit_names[i]).orElseGet(Unit::new));
            serviceList.add(service);
        }

        return serviceRepository.saveAll(serviceList);
    }
}
