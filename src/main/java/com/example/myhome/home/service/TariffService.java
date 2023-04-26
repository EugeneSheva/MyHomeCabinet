package com.example.myhome.home.service;

import com.example.myhome.home.model.Service;
import com.example.myhome.home.model.Tariff;
import com.example.myhome.home.model.TariffComponents;

import java.util.List;
import java.util.Map;

public interface TariffService {

    List<Tariff> findAllTariffs();
    List<Tariff> findAllTariffsSorted(Boolean order);

    Tariff findTariffById(Long tariff_id);
    Tariff saveTariff(Tariff tariff);
    void deleteTariffById(Long tariff_id);

    List<TariffComponents> buildComponentsList(String[] names, String[] prices);
    Map<Service, Double> buildComponentsMap(String[] names, String[] prices);


}
