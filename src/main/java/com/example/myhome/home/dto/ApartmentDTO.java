package com.example.myhome.home.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApartmentDTO {

    private Long id;

    private String section;
    private String floor;
    private Long number;
    private Double balance;
    private Double square;

    private String fullName;

    private BuildingDTO building;
    private ApartmentAccountDTO account;
    private OwnerDTO owner;

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
