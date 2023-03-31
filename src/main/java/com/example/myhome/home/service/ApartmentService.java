package com.example.myhome.home.service;

import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.model.ApartmentDTO;
import com.example.myhome.home.model.Building;
import com.example.myhome.home.model.BuildingDTO;
import com.example.myhome.home.repository.ApartmentRepository;
import com.example.myhome.home.repository.BuildingRepository;
import com.example.myhome.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApartmentService {
    @Value("${upload.path}")
    private String uploadPath;
    private String localPath = "/img/apartment/";
    private final ApartmentRepository apartmentRepository;
    private final FileUploadUtil fileUploadUtil;

    public Apartment findById (Long id) { return apartmentRepository.findById(id).orElseThrow(() -> new NotFoundException());}

    public Apartment save(Apartment apartment) { return apartmentRepository.save(apartment); }

    public void deleteById(Long id) { apartmentRepository.deleteById(id); }

    public List<Apartment> findAll() { return apartmentRepository.findAll(); }
    public List<ApartmentDTO> findDtoApartments() {
        List<ApartmentDTO>apartmentDTOList= new ArrayList<>();
        for (Apartment apartment : apartmentRepository.findAll()) {
            apartmentDTOList.add(new ApartmentDTO(apartment.getId(),apartment.getBuilding().getId(), apartment.getSection(),
                    apartment.getFloor(), apartment.getNumber(), apartment.getBalance(), apartment.getAccount().getNumber(), apartment.getOwner().getId()));
        }
        return apartmentDTOList; }


    public List<ApartmentDTO> findDtoApartmentsWithDebt() {
        List<ApartmentDTO>apartmentDTOList= new ArrayList<>();
        for (Apartment apartment : apartmentRepository.findApartmentsByBalanceBefore(0D)) {
            apartmentDTOList.add(new ApartmentDTO(apartment.getId(),apartment.getBuilding().getId(), apartment.getSection(),
                    apartment.getFloor(), apartment.getNumber(), apartment.getBalance(), apartment.getAccount().getNumber(), apartment.getOwner().getId()));
        }
        return apartmentDTOList; }

    public List<ApartmentDTO> findDtoApartmentsByBuilding(Long building_id) {
        System.out.println("start service");
        List<ApartmentDTO>apartmentDTOList= new ArrayList<>();
        for (Apartment apartment : apartmentRepository.findApartmentsByBuildingId(building_id)) {
            apartmentDTOList.add(new ApartmentDTO(apartment.getId(),apartment.getBuilding().getId(), apartment.getSection(),
                    apartment.getFloor(), apartment.getNumber(), apartment.getBalance(), apartment.getAccount().getNumber(), apartment.getOwner().getId()));
        }
        System.out.println("apartmentDTOList " + apartmentDTOList);
        return apartmentDTOList; }

    public List<ApartmentDTO> findDtoApartmentsByBuildingWithDebt(Long building_id) {
        List<ApartmentDTO>apartmentDTOList= new ArrayList<>();
        for (Apartment apartment : apartmentRepository.findApartmentsByBuildingIdAndBalanceBefore(building_id, 0D)) {
            apartmentDTOList.add(new ApartmentDTO(apartment.getId(),apartment.getBuilding().getId(), apartment.getSection(),
                    apartment.getFloor(), apartment.getNumber(), apartment.getBalance(), apartment.getAccount().getNumber(), apartment.getOwner().getId()));
        }
        return apartmentDTOList; }

    public List<ApartmentDTO> findDtoApartmentsByBuildingAndSection(Long building_id, String section) {
        List<ApartmentDTO>apartmentDTOList= new ArrayList<>();
        for (Apartment apartment : apartmentRepository.findApartmentsByBuildingIdAndSectionContainingIgnoreCase(building_id, section)) {
            apartmentDTOList.add(new ApartmentDTO(apartment.getId(),apartment.getBuilding().getId(), apartment.getSection(),
                    apartment.getFloor(), apartment.getNumber(), apartment.getBalance(), apartment.getAccount().getNumber(), apartment.getOwner().getId()));
        }
        return apartmentDTOList; }

    public List<ApartmentDTO> findDtoApartmentsByBuildingAndSectionWithDebt(Long building_id, String section) {
        List<ApartmentDTO>apartmentDTOList= new ArrayList<>();
        for (Apartment apartment : apartmentRepository.findApartmentsByBuildingIdAndSectionContainingIgnoreCaseAndBalanceBefore(building_id, section, 0D)) {
            apartmentDTOList.add(new ApartmentDTO(apartment.getId(),apartment.getBuilding().getId(), apartment.getSection(),
                    apartment.getFloor(), apartment.getNumber(), apartment.getBalance(), apartment.getAccount().getNumber(), apartment.getOwner().getId()));
        }
        return apartmentDTOList; }

    public List<ApartmentDTO> findDtoApartmentsByBuildingAndFloor(Long building_id, String floor) {
        List<ApartmentDTO>apartmentDTOList= new ArrayList<>();
        for (Apartment apartment : apartmentRepository.findApartmentsByBuildingIdAndFloorContainingIgnoreCase(building_id, floor)) {
            apartmentDTOList.add(new ApartmentDTO(apartment.getId(),apartment.getBuilding().getId(), apartment.getSection(),
                    apartment.getFloor(), apartment.getNumber(), apartment.getBalance(), apartment.getAccount().getNumber(), apartment.getOwner().getId()));
        }
        return apartmentDTOList; }

    public List<ApartmentDTO> findDtoApartmentsByBuildingAndFloorWithDebt(Long building_id, String floor) {
        List<ApartmentDTO>apartmentDTOList= new ArrayList<>();
        for (Apartment apartment : apartmentRepository.findApartmentsByBuildingIdAndFloorContainingIgnoreCaseAndBalanceBefore(building_id, floor,0D)) {
            apartmentDTOList.add(new ApartmentDTO(apartment.getId(),apartment.getBuilding().getId(), apartment.getSection(),
                    apartment.getFloor(), apartment.getNumber(), apartment.getBalance(), apartment.getAccount().getNumber(), apartment.getOwner().getId()));
        }
        return apartmentDTOList; }

    public List<ApartmentDTO> findDtoApartmentsByBuildingAndSectionAndFloor(Long building_id, String section, String floor) {
        List<ApartmentDTO>apartmentDTOList= new ArrayList<>();
        for (Apartment apartment : apartmentRepository.findApartmentsByBuildingIdAndSectionContainingIgnoreCaseAndFloorContainingIgnoreCase(building_id, section, floor)) {
            apartmentDTOList.add(new ApartmentDTO(apartment.getId(),apartment.getBuilding().getId(), apartment.getSection(),
                    apartment.getFloor(), apartment.getNumber(), apartment.getBalance(), apartment.getAccount().getNumber(), apartment.getOwner().getId()));
        }
        return apartmentDTOList; }

    public List<ApartmentDTO> findDtoApartmentsByBuildingAndSectionAndFloorWithDebt(Long building_id, String section, String floor) {
        List<ApartmentDTO>apartmentDTOList= new ArrayList<>();
        for (Apartment apartment : apartmentRepository.findApartmentsByBuildingIdAndSectionContainingIgnoreCaseAndFloorContainingIgnoreCaseAndBalanceBefore(building_id, section, floor,0D)) {
            apartmentDTOList.add(new ApartmentDTO(apartment.getId(),apartment.getBuilding().getId(), apartment.getSection(),
                    apartment.getFloor(), apartment.getNumber(), apartment.getBalance(), apartment.getAccount().getNumber(), apartment.getOwner().getId()));
        }
        return apartmentDTOList; }
//
}
