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

    public ApartmentAccountDTO(Long id, Boolean isActive, Long number, Double balance) {
        this.id = id;
        this.isActive = isActive;
        this.number = number;
        this.balance = balance;
    }
}
