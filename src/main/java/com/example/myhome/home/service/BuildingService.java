package com.example.myhome.home.service;

import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.model.Building;
import com.example.myhome.home.repository.BuildingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class BuildingService {

    private final BuildingRepository buildingRepository;


    public Building findById (Long id) { return buildingRepository.findById(id).orElseThrow(() -> new NotFoundException());}

    public List<Building> findAll() { return buildingRepository.findAll(); }

    public Building save(Building building) { return buildingRepository.save(building); }

    public void deleteById(Long id) { buildingRepository.deleteById(id); }
}
