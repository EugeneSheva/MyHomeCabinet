package com.example.myhome.home.service.impl;

import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.mapper.ApartmentDTOMapper;
import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.dto.ApartmentDTO;
import com.example.myhome.home.repository.ApartmentRepository;
import com.example.myhome.home.service.ApartmentService;
import com.example.myhome.util.FileUploadUtil;
import com.example.myhome.util.MappingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApartmentServiceImpl implements ApartmentService {
    @Value("${upload.path}")
    private String uploadPath;
    private String localPath = "/img/apartment/";
    private final ApartmentRepository apartmentRepository;
    private final FileUploadUtil fileUploadUtil;


    private final ApartmentDTOMapper mapper;

    public Apartment findById(Long id) {
        return (id == null) ? null : apartmentRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public Apartment findByNumber(Long number) {
        return apartmentRepository.findByNumber(number).orElse(null);
    }

//    public Apartment save(Apartment apartment) {
//        return apartmentRepository.save(apartment);
//    }
//
//    public void deleteById(Long id) {
//        apartmentRepository.deleteById(id);
//    }
//
//    public Long getNumberById(Long id) {
//        return apartmentRepository.findById(id).orElseThrow().getNumber();
//    }
//
//    public List<Apartment> findAll() {
//        return apartmentRepository.findAll();
//    }
//
//    public Page<Apartment> findAll(Pageable pageable) {
//        return apartmentRepository.findAll(pageable);
//    }

//    public List<ApartmentDTO> findDtoApartments() {
//        return apartmentRepository.findAll().stream().map(MappingUtils::fromApartmentToDTO).collect(Collectors.toList());
//    }


    public List<ApartmentDTO> convertApartmentsToApartmentsDTO(List<Apartment> apartmentList) {
        return apartmentList.stream().map(MappingUtils::fromApartmentToDTO).collect(Collectors.toList());
    }

//    public ApartmentDTO convertApartmentsToApartmentsDTO(Apartment apartment) {
//        return MappingUtils.fromApartmentToDTO(apartment);
//    }


    public ApartmentDTO findApartmentDto(Long id) {
        Apartment apartment = apartmentRepository.findById(id).orElseThrow();
        return mapper.fromApartmentToDTO(apartment);
    }


//    public List<ApartmentDTO> findDtoApartmentsWithDebt() {
//        return apartmentRepository.findApartmentsByBalanceBefore(0D).stream().map(MappingUtils::fromApartmentToDTO).collect(Collectors.toList());
//    }
//
//    public List<ApartmentDTO> findDtoApartmentsByBuilding(Long building_id) {
//        return apartmentRepository.findApartmentsByBuildingId(building_id).stream().map(MappingUtils::fromApartmentToDTO).collect(Collectors.toList());
//    }
//
//    public List<ApartmentDTO> findDtoApartmentsByBuildingWithDebt(Long building_id) {
//        return apartmentRepository.findApartmentsByBuildingIdAndBalanceBefore(building_id, 0D).stream().map(MappingUtils::fromApartmentToDTO).collect(Collectors.toList());
//    }
//
//    public List<ApartmentDTO> findDtoApartmentsByBuildingAndSection(Long building_id, String section) {
//        return apartmentRepository.findApartmentsByBuildingIdAndSectionContainingIgnoreCase(building_id, section).stream()
//                .map(MappingUtils::fromApartmentToDTO).collect(Collectors.toList());
//    }
//
//    public List<ApartmentDTO> findDtoApartmentsByBuildingAndSectionWithDebt(Long building_id, String section) {
//        return apartmentRepository.findApartmentsByBuildingIdAndSectionContainingIgnoreCaseAndBalanceBefore(building_id, section, 0D)
//                .stream().map(MappingUtils::fromApartmentToDTO).collect(Collectors.toList());
//    }
//
//    public List<ApartmentDTO> findDtoApartmentsByBuildingAndFloor(Long building_id, String floor) {
//        return apartmentRepository.findApartmentsByBuildingIdAndFloorContainingIgnoreCase(building_id, floor)
//                .stream().map(MappingUtils::fromApartmentToDTO).collect(Collectors.toList());
//    }
//
//    public List<ApartmentDTO> findDtoApartmentsByBuildingAndFloorWithDebt(Long building_id, String floor) {
//        return apartmentRepository.findApartmentsByBuildingIdAndFloorContainingIgnoreCaseAndBalanceBefore(building_id, floor, 0D)
//                .stream().map(MappingUtils::fromApartmentToDTO).collect(Collectors.toList());
//    }
//
//    public List<ApartmentDTO> findDtoApartmentsByBuildingAndSectionAndFloor(Long building_id, String section, String floor) {
//        return apartmentRepository.findApartmentsByBuildingIdAndSectionContainingIgnoreCaseAndFloorContainingIgnoreCase(building_id, section, floor)
//                .stream().map(MappingUtils::fromApartmentToDTO).collect(Collectors.toList());
//    }
//
//    public List<ApartmentDTO> findDtoApartmentsByBuildingAndSectionAndFloorWithDebt(Long building_id, String section, String floor) {
//        return apartmentRepository.findApartmentsByBuildingIdAndSectionContainingIgnoreCaseAndFloorContainingIgnoreCaseAndBalanceBefore(building_id, section, floor, 0D)
//                .stream().map(MappingUtils::fromApartmentToDTO).collect(Collectors.toList());
//    }
//
//    public Long getQuantity() {
//        return apartmentRepository.countAllBy();
//    }
//
//    public Page<ApartmentDTO> findBySpecificationAndPage(Integer page, Integer size, FilterForm filters) {
//        Pageable pageable = PageRequest.of(page - 1, size);
//        List<ApartmentDTO> listDTO = new ArrayList<>();
//        Page<Apartment> apartments = apartmentRepository.findByFilters(filters.getNumber(), filters.getBuildingName(), filters.getSection(),
//                filters.getFloor(), filters.getOwner(), filters.getDebtSting(), pageable);
//        for (Apartment apartment : apartments) {
//            Owner owner = apartment.getOwner();
//            Building building = apartment.getBuilding();
//            listDTO.add(new ApartmentDTO(apartment.getId(), new BuildingDTO(building.getId(), building.getName()), apartment.getSection(), apartment.getFloor(),
//                    apartment.getNumber(), apartment.getAccount().getId(), new OwnerDTO(owner.getId(), owner.getFirst_name(), owner.getLast_name(), owner.getFathers_name(), owner.getFullName()), apartment.getBalance()));
//        }
//        return new PageImpl<>(listDTO, pageable, apartments.getTotalElements());
//    }
//
}
