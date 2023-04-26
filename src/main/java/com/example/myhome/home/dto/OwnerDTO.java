package com.example.myhome.home.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

// --- ВЛАДЕЛЬЦЫ КВАРТИР ---

@Data
@AllArgsConstructor
@Builder
public class OwnerDTO {

    private Long id = 0L;
    private String text = "";

    private String first_name = "", last_name = "", fathers_name = "";
    private String fullName = "";

    private String phone_number = "";
    private String email = "";

    private List<BuildingDTO> buildings = new ArrayList<>();
    private List<ApartmentDTO> apartments = new ArrayList<>();

    private String apartmentNumbers = "";
    private String apartmentNames = "";

    private String date = "";

    private String status = "";

    private Boolean hasDebt = false;


    public OwnerDTO() {
    }

    public OwnerDTO(Long id, String first_name, String last_name, String fathers_name) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.fathers_name = fathers_name;
        this.text = this.first_name + " " + this.fathers_name + " " + this.last_name + "(ID:"+this.id+")";
    }
}
