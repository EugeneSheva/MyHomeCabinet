package com.example.myhome.home.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ApartmentDTO {

    private Long id;

    private Long buildingId;

    private BuildingDTO building;
    private String section;
    private String floor;
    private Long number;
    private Double balance;

    private String fullName;

    private Long account;



    private Long ownerId;
    private OwnerDTO owner;

    public ApartmentDTO(Long id, Long buildingId, String section, String floor, Long number, Double balance, Long account, Long ownerId) {
        this.id = id;
        this.buildingId = buildingId;
        this.section = section;
        this.floor = floor;
        this.number = number;
        this.balance = balance;
        this.account = account;
        this.ownerId = ownerId;
    }

    public ApartmentDTO(Long id, BuildingDTO building, String section, String floor, Long number, Double balance, Long account, Long ownerId) {
        this.id = id;
        this.building = building;
        this.section = section;
        this.floor = floor;
        this.number = number;
        this.balance = balance;
        this.account = account;
        this.ownerId = ownerId;
    }

    public ApartmentDTO(Long id, BuildingDTO buildingDTO, String section, String floor, Long number, Long account, OwnerDTO ownerDTO) {
        this.id = id;
        this.building = buildingDTO;
        this.section = section;
        this.floor = floor;
        this.number = number;
        this.account = account;
        this.owner = ownerDTO;
    }

    public ApartmentDTO(Long id, BuildingDTO buildingDTO, String section, String floor, Long number, Long account, OwnerDTO ownerDTO, Double balance) {
        this.id = id;
        this.building = buildingDTO;
        this.section = section;
        this.floor = floor;
        this.number = number;
        this.account = account;
        this.owner = ownerDTO;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "ApartmentDTO{" +
                "id=" + id +
                ", section=" + section +
                ", floor=" + floor +
                ", number=" + number +
                ", ownerId=" + ownerId +
                ", balance=" + balance +
                "}\n";
    }
}
