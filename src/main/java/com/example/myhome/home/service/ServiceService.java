package com.example.myhome.home.service;

import com.example.myhome.home.model.Service;
import com.example.myhome.home.model.Unit;

import java.util.List;

public interface ServiceService {

    List<Service> findAllServices();
    List<Unit> findAllUnits();

    Service findServiceById(Long service_id);
    String getServiceNameById(Long service_id);
    String getUnitNameById(Long unit_id);

    Service saveService(Service service);
    void deleteServiceById(Long service_id);

    List<Unit> addNewUnits(List<Unit> unitList, String[] new_unit_names);
    List<Service> addNewServices(List<Service> serviceList,
                                 String[] new_service_names,
                                 String[] new_service_unit_names,
                                 String[] new_service_show_in_meters);

}
