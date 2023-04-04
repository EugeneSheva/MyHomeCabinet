package com.example.myhome.home.service;

import com.example.myhome.home.exception.EmptyObjectException;
import com.example.myhome.home.model.Tariff;
import com.example.myhome.home.model.TariffComponents;
import com.example.myhome.home.repository.ServiceRepository;
import com.example.myhome.home.repository.TariffRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log
public class TariffService {

    @Autowired
    private TariffRepository tariffRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    public List<Tariff> findAllTariffs() {return tariffRepository.findAll();}

    public Tariff findTariffById(long tariff_id) {return tariffRepository.findById(tariff_id).orElseThrow();}

    public Tariff saveTariff(Tariff tariff) {
        if(tariff.getTariffComponents().size() == 0) {
            throw new EmptyObjectException("Can't save tariff without components");
        }
        return tariffRepository.save(tariff);
    }

    public void deleteTariff(long tariff_id) {tariffRepository.deleteById(tariff_id);}

    public List<TariffComponents> getComponentsInList(String[] service_names, String[] prices) {
        log.info("Getting components from arrays into list");

        List<TariffComponents> list = new ArrayList<>();

        for (int i = 0; i < service_names.length; i++) {
            if(service_names[i].isEmpty() || prices[i].isEmpty()) continue;
            list.add(new TariffComponents(service_names[i], Double.parseDouble(prices[i])));
            log.info("Added component: " + service_names[i] + ", price - " + prices[i]);
        }

        return list;
    }

    public Map<com.example.myhome.home.model.Service, Double> getComponentsInMap(String[] service_names, String[] prices) {
        if(service_names == null || prices == null) {
            log.info("Sent empty arrays, exiting with empty map");
            return new HashMap<>();
        }

        log.info("Creating a map for components from arrays");
        Map<com.example.myhome.home.model.Service, Double> map = new HashMap<>();

        for(int i = 0; i < service_names.length; i++) {
            if(service_names[i].isEmpty() || prices[i].isEmpty()) continue;
            Optional<com.example.myhome.home.model.Service> s = serviceRepository.findByName(service_names[i]);
            if(s.isEmpty()) {log.info("Service not found from name: " + service_names[i] + ", skip"); continue;}
            try {
                Double price = Double.parseDouble(prices[i]);
                map.put(s.get(), price);
            } catch (NumberFormatException e) {
                log.info("Price can't be made into double: " + prices[i] + ", skip");
                continue;
            }
            log.info("Added component: " + service_names[i] + ", price - " + prices[i]);
        }

        return map;
    }
}
