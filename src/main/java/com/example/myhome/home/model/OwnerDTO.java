package com.example.myhome.home.model;

import com.example.myhome.home.dto.ApartmentDTO;
import com.example.myhome.home.dto.BuildingDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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
//    private List<Apartment> apartments;
    private List<Message> messages;
    private String phone_number;
    private String email;
    private String viber, telegram;
    private String description;
    private String profile_picture;
    private List<BuildingDTO> buildings;
    private List<ApartmentDTO> apartments;
    private String apartmentNumbers;
    private String apartmentNames;
    private String date;
    private String status;
    public OwnerDTO(Long id, String first_name, String last_name, String fathers_name, String fullName, List<ApartmentDTO> apartments, List<Message> messages, String phone_number, String email, String viber, String telegram, String description, String profile_picture) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.fathers_name = fathers_name;
        this.fullName = fullName;
        this.apartments = apartments;
        this.messages = messages;
    }

    public OwnerDTO(Long id, String first_name, String last_name, String fathers_name, String fullName, String phone_number, String email, String viber, String telegram, String description, List<ApartmentDTO> apartments, List<BuildingDTO> buildings , String date, String status, Boolean hasDebt) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.fathers_name = fathers_name;
        this.fullName = fullName;
        this.phone_number = phone_number;
        this.email = email;
        this.viber = viber;
        this.telegram = telegram;
        this.description = description;
        this.apartments = apartments;
        this.buildings = buildings;
        this.date = date;
        this.status = status;
        this.hasDebt = hasDebt;
    }

    public OwnerDTO(Long id, String first_name, String last_name, String fathers_name, String fullName, List<ApartmentDTO> apartments) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.fathers_name = fathers_name;
        this.fullName = fullName;
        this.apartments = apartments;
    }
    private Boolean hasDebt;

    public OwnerDTO(Long id, String first_name, String last_name, String fathers_name, String fullName) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.fathers_name = fathers_name;
        this.fullName = fullName;
    }

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
