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

    private Long building;
    private String section;
    private String floor;
    private Long number;
    private Double balance;

    private String fullName;

    private Long account;
    private Long owner;




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
