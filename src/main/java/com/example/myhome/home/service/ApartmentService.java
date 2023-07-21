package com.example.myhome.home.service;

import com.example.myhome.home.dto.ApartmentDTO;
import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.model.filter.FilterForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ApartmentService {

    Apartment findById(Long id);
    Apartment findByNumber(Long number);
    ApartmentDTO findApartmentDto(Long id);
    List<ApartmentDTO> convertApartmentsToApartmentsDTO(List<Apartment> apartmentList);




}
