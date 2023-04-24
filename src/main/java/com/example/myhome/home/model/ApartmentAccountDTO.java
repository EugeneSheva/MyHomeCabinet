package com.example.myhome.home.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

// --- ЛИЦЕВЫЕ СЧЕТА ---

@Data
@AllArgsConstructor
@Builder
public class ApartmentAccountDTO {

    private Long id;
    private Boolean isActive;
    private Long number;
    private Double balance = 0.0;

    private ApartmentDTO apartment;
    private BuildingDTO building;
    private OwnerDTO owner;


}
