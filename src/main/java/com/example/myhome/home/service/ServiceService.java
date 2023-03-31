package com.example.myhome.home.service;


import com.example.myhome.home.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.myhome.home.model.Service;



import java.util.List;

@org.springframework.stereotype.Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public List<Service> findAllServices() {return serviceRepository.findAll();}

    public Service findServiceById(long service_id) {return serviceRepository.findById(service_id).orElseThrow();}

    public Service saveService(Service service) {return serviceRepository.save(service);}

    public void deleteServiceById(long service_id) {serviceRepository.deleteById(service_id);}
}
