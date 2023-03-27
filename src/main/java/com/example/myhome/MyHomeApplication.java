package com.example.myhome;

import com.example.myhome.home.model.*;
import com.example.myhome.home.repos.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
@Log
public class MyHomeApplication {

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private BuildingSectionRepository buildingSectionRepository;

    @Autowired
    private BuildingFloorsRepository buildingFloorsRepository;

    public static void main(String[] args) {
        SpringApplication.run(MyHomeApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void populateDB(){

        //buildingRepository.deleteAll();
        //ownerRepository.deleteAll();
        //apartmentRepository.deleteAll();

//        List<Building> buildingList = new ArrayList<>();
//        List<Owner> ownerList = new ArrayList<>();

//        log.info("CREATING START BUILDINGS");
//        for (long i = 1; i <= 5; i++) {
//            Building building =
//                    new Building(i,
//                            "ЖК \"Тест-"+i+"\"",
//                            "Тестовый адрес");
//            buildingList.add(building);
//        }
//        buildingRepository.saveAll(buildingList);
//
//        log.info("FILLING BUILDINGS WITH SECTIONS AND FLOORS");
//        List<Building> buildings2 = buildingRepository.findAll();
//        for(int i = 0; i < 5; i++) {
//            List<BuildingSection> sections = new ArrayList<>();
//            List<BuildingFloor> floors = new ArrayList<>();
//            for (int j = 0; j < 5; j++) {
//                BuildingSection section = new BuildingSection(0L, buildings2.get(i), "Секция " + j);
//                sections.add(section);
//                BuildingFloor floor = new BuildingFloor(0L, buildings2.get(i), "Этажик " + j);
//                floors.add(floor);
//            }
//            buildingSectionRepository.saveAll(sections);
//            buildingFloorsRepository.saveAll(floors);
//        }

//        log.info("CREATING START OWNERS");
//        for (long i = 1; i <= 15; i++) {
//            Owner owner =
//                    new Owner(i,
//                            "Тест", "Тест", "Тестович",
//                            "+380501111111",
//                            "test@gmail.com", "testPassword");
//            ownerList.add(owner);
//        }
//        ownerRepository.saveAll(ownerList);

//        List<Apartment> apartmentList = new ArrayList<>();
//        List<Building> buildings = buildingRepository.findAll();
//        List<Owner> owners = ownerRepository.findAll();
////
//        log.info("CREATING START APARTMENTS");
//        for(int i = 0; i < 15; i++) {
//
//            Building building = buildings.get((i/3));
//            Owner owner = owners.get(i);
//
//            log.info(building.toString());
//            log.info(owner.toString());
//
//            Apartment apartment =
//                    new Apartment((long) i,
//                            building,
//                            building.getSections().get(0),
//                            building.getFloors().get(0),
//                            (long) (i+50),
//                            (double)i*100,
//                            (double)i*20,
//                            owner);
//            apartmentList.add(apartment);
//
//            log.info(apartment.toString());
//        }
//        apartmentRepository.saveAll(apartmentList);
//
//        log.info(apartmentList.toString());
    }

}
