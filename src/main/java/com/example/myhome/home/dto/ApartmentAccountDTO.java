package com.example.myhome.home.dto;

import lombok.*;

// --- ЛИЦЕВЫЕ СЧЕТА ---

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApartmentAccountDTO {

    private Long id;
    private Boolean isActive;
    private Boolean changedState = false;
    private Long number;
    private Double balance = 0.0;
    private String section;

    private ApartmentDTO apartment;
    private BuildingDTO building;
    private OwnerDTO owner;


}
