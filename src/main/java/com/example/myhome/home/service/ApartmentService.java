package com.example.myhome.home.service;

import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.model.ApartmentDTO;
import com.example.myhome.home.repository.ApartmentRepository;
import com.example.myhome.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApartmentService {
    @Value("${upload.path}")
    private String uploadPath;
    private String localPath = "/img/apartment/";
    private final ApartmentRepository apartmentRepository;
    private final FileUploadUtil fileUploadUtil;


    public Apartment findById (Long id) { return (id == null) ? null : apartmentRepository.findById(id).orElseThrow(NotFoundException::new);}
    public Apartment findByNumber(Long number) {return apartmentRepository.findByNumber(number).orElse(null);}

    public Apartment save(Apartment apartment) { return apartmentRepository.save(apartment); }

    public void deleteById(Long id) { apartmentRepository.deleteById(id); }

    public Long getNumberById(Long id) {return apartmentRepository.findById(id).orElseThrow().getNumber();}

    public List<Apartment> findAll() { return apartmentRepository.findAll(); }

    public Page<Apartment> findAll(Pageable pageable) { return apartmentRepository.findAll(pageable); }
    public List<ApartmentDTO> findDtoApartments() {
        List<ApartmentDTO>apartmentDTOList= new ArrayList<>();
        for (Apartment apartment : apartmentRepository.findAll()) {
//            apartmentDTOList.add(new ApartmentDTO(apartment.getId(),apartment.getBuilding().getId(), apartment.getSection(),
//                    apartment.getFloor(), apartment.getNumber(), apartment.getBalance(), apartment.getAccount().getNumber(), apartment.getOwner().getId()));
        }
        return apartmentDTOList; }


    public List<ApartmentDTO> findDtoApartmentsWithDebt() {
        List<ApartmentDTO>apartmentDTOList= new ArrayList<>();
        for (Apartment apartment : apartmentRepository.findApartmentsByBalanceBefore(0D)) {
//            apartmentDTOList.add(new ApartmentDTO(apartment.getId(),apartment.getBuilding().getId(), apartment.getSection(),
//                    apartment.getFloor(), apartment.getNumber(), apartment.getBalance(), apartment.getAccount().getNumber(), apartment.getOwner().getId()));
        }
        return apartmentDTOList; }

    public List<ApartmentDTO> findDtoApartmentsByBuilding(Long building_id) {
        System.out.println("start service");
        List<ApartmentDTO>apartmentDTOList= new ArrayList<>();
        for (Apartment apartment : apartmentRepository.findApartmentsByBuildingId(building_id)) {
//            apartmentDTOList.add(new ApartmentDTO(apartment.getId(),apartment.getBuilding().getId(), apartment.getSection(),
//                    apartment.getFloor(), apartment.getNumber(), apartment.getBalance(), apartment.getAccount().getNumber(), apartment.getOwner().getId()));
        }
        System.out.println("apartmentDTOList " + apartmentDTOList);
        return apartmentDTOList; }

    public List<ApartmentDTO> findDtoApartmentsByBuildingWithDebt(Long building_id) {
        List<ApartmentDTO>apartmentDTOList= new ArrayList<>();
        for (Apartment apartment : apartmentRepository.findApartmentsByBuildingIdAndBalanceBefore(building_id, 0D)) {
//            apartmentDTOList.add(new ApartmentDTO(apartment.getId(),apartment.getBuilding().getId(), apartment.getSection(),
//                    apartment.getFloor(), apartment.getNumber(), apartment.getBalance(), apartment.getAccount().getNumber(), apartment.getOwner().getId()));
        }
        return apartmentDTOList; }

    public List<ApartmentDTO> findDtoApartmentsByBuildingAndSection(Long building_id, String section) {
        List<ApartmentDTO>apartmentDTOList= new ArrayList<>();
        for (Apartment apartment : apartmentRepository.findApartmentsByBuildingIdAndSectionContainingIgnoreCase(building_id, section)) {
//            apartmentDTOList.add(new ApartmentDTO(apartment.getId(),apartment.getBuilding().getId(), apartment.getSection(),
//                    apartment.getFloor(), apartment.getNumber(), apartment.getBalance(), apartment.getAccount().getNumber(), apartment.getOwner().getId()));
        }
        return apartmentDTOList; }

    public List<ApartmentDTO> findDtoApartmentsByBuildingAndSectionWithDebt(Long building_id, String section) {
        List<ApartmentDTO>apartmentDTOList= new ArrayList<>();
        for (Apartment apartment : apartmentRepository.findApartmentsByBuildingIdAndSectionContainingIgnoreCaseAndBalanceBefore(building_id, section, 0D)) {
//            apartmentDTOList.add(new ApartmentDTO(apartment.getId(),apartment.getBuilding().getId(), apartment.getSection(),
//                    apartment.getFloor(), apartment.getNumber(), apartment.getBalance(), apartment.getAccount().getNumber(), apartment.getOwner().getId()));
        }
        return apartmentDTOList; }

    public List<ApartmentDTO> findDtoApartmentsByBuildingAndFloor(Long building_id, String floor) {
        List<ApartmentDTO>apartmentDTOList= new ArrayList<>();
        for (Apartment apartment : apartmentRepository.findApartmentsByBuildingIdAndFloorContainingIgnoreCase(building_id, floor)) {
//            apartmentDTOList.add(new ApartmentDTO(apartment.getId(),apartment.getBuilding().getId(), apartment.getSection(),
//                    apartment.getFloor(), apartment.getNumber(), apartment.getBalance(), apartment.getAccount().getNumber(), apartment.getOwner().getId()));
        }
        return apartmentDTOList; }

    public List<ApartmentDTO> findDtoApartmentsByBuildingAndFloorWithDebt(Long building_id, String floor) {
        List<ApartmentDTO>apartmentDTOList= new ArrayList<>();
        for (Apartment apartment : apartmentRepository.findApartmentsByBuildingIdAndFloorContainingIgnoreCaseAndBalanceBefore(building_id, floor,0D)) {
//            apartmentDTOList.add(new ApartmentDTO(apartment.getId(),apartment.getBuilding().getId(), apartment.getSection(),
//                    apartment.getFloor(), apartment.getNumber(), apartment.getBalance(), apartment.getAccount().getNumber(), apartment.getOwner().getId()));
        }
        return apartmentDTOList; }

    public List<ApartmentDTO> findDtoApartmentsByBuildingAndSectionAndFloor(Long building_id, String section, String floor) {
        List<ApartmentDTO>apartmentDTOList= new ArrayList<>();
        for (Apartment apartment : apartmentRepository.findApartmentsByBuildingIdAndSectionContainingIgnoreCaseAndFloorContainingIgnoreCase(building_id, section, floor)) {
//            apartmentDTOList.add(new ApartmentDTO(apartment.getId(),apartment.getBuilding().getId(), apartment.getSection(),
//                    apartment.getFloor(), apartment.getNumber(), apartment.getBalance(), apartment.getAccount().getNumber(), apartment.getOwner().getId()));
        }
        return apartmentDTOList; }

    public List<ApartmentDTO> findDtoApartmentsByBuildingAndSectionAndFloorWithDebt(Long building_id, String section, String floor) {
        List<ApartmentDTO>apartmentDTOList= new ArrayList<>();
        for (Apartment apartment : apartmentRepository.findApartmentsByBuildingIdAndSectionContainingIgnoreCaseAndFloorContainingIgnoreCaseAndBalanceBefore(building_id, section, floor,0D)) {
//            apartmentDTOList.add(new ApartmentDTO(apartment.getId(),apartment.getBuilding().getId(), apartment.getSection(),
//                    apartment.getFloor(), apartment.getNumber(), apartment.getBalance(), apartment.getAccount().getNumber(), apartment.getOwner().getId()));
        }
        return apartmentDTOList; }

    public Long getQuantity() { return apartmentRepository.countAllBy();}
//
}
