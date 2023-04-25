package com.example.myhome.home.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

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
    private Long owner;

    public ApartmentDTO(Long id, Long buildingId, String section, String floor, Long number, Double balance, Long account, Long owner) {
        this.id = id;
        this.buildingId = buildingId;
        this.section = section;
        this.floor = floor;
        this.number = number;
        this.balance = balance;
        this.account = account;
        this.owner = owner;
    }

    public ApartmentDTO(Long id, BuildingDTO building, String section, String floor, Long number, Double balance, Long account, Long owner) {
        this.id = id;
        this.building = building;
        this.section = section;
        this.floor = floor;
        this.number = number;
        this.balance = balance;
        this.account = account;
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "ApartmentDTO{" +
                "id=" + id +
                ", section=" + section +
                ", floor=" + floor +
                ", number=" + number +
                ", owner=" + owner +
                ", balance=" + balance +
                "}\n";
    }
}
