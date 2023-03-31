package com.example.myhome;

import com.example.myhome.home.model.*;
import com.example.myhome.home.repository.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication(scanBasePackages = {"com.example.myhome.home", "com.example.myhome.util"})
@EnableJpaRepositories(basePackages = "com.example.myhome.home.repository")
@Log
public class MyHomeApplication {

//    @Autowired
//    private BuildingRepository buildingRepository;
//
//    @Autowired
//    private ApartmentRepository apartmentRepository;
//
//    @Autowired
//    private OwnerRepository ownerRepository;


    public static void main(String[] args) {
        SpringApplication.run(MyHomeApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void populateDB(){

//        buildingRepository.deleteAll();
////        ownerRepository.deleteAll();
//        apartmentRepository.deleteAll();
//
//        List<Building> buildingList = new ArrayList<>();
////        List<Owner> ownerList = new ArrayList<>();
//
//        log.info("CREATING START BUILDINGS");
//        for (long i = 1; i <= 5; i++) {
//            Building building =
//                    new Building(i,
//                            "ЖК \"Тест-"+i+"\"",
//                            "Тестовый адрес",
//                            List.of("Секция 1", "Секция 2", "Секция 3"),
//                            List.of("Этажик 1", "Этажик 2", "Этажик 3"));
//            buildingList.add(building);
//        }
//        buildingRepository.saveAll(buildingList);
//
////        log.info("CREATING START OWNERS");
////        for (long i = 1; i <= 15; i++) {
////            Owner owner =
////                    new Owner(i,
////                            "Тест", "Тест", "Тестович",
////                            "+380501111111",
////                            "test@gmail.com", "testPassword");
////            ownerList.add(owner);
////        }
////        ownerRepository.saveAll(ownerList);
//
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
