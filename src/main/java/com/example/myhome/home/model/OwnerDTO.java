package com.example.myhome.home.model;

import com.example.myhome.util.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// --- ВЛАДЕЛЬЦЫ КВАРТИР ---

@Data
@AllArgsConstructor
@Builder
public class OwnerDTO {

    private Long id;
    private String text;

    private String first_name, last_name, fathers_name;
    private String fullName;

    private String phone_number;
    private String email;

    private List<BuildingDTO> buildings;
    private List<ApartmentDTO> apartments;

    private String apartmentNumbers;
    private String apartmentNames;

    private String date;

    private String status;

    private Boolean hasDebt;


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
