package com.example.myhome.home.service;

import com.example.myhome.home.model.Tariff;
import com.example.myhome.home.repository.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TariffService {

    @Autowired
    private TariffRepository tariffRepository;

    public List<Tariff> findAllTariffs() {return tariffRepository.findAll();}

    public Tariff findTariffById(long tariff_id) {return tariffRepository.findById(tariff_id).orElseThrow();}

    public Tariff saveTariff(Tariff tariff) {return tariffRepository.save(tariff);}

    public void deleteTariff(long tariff_id) {tariffRepository.deleteById(tariff_id);}


}
