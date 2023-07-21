package com.example.myhome.home.dto;


import com.example.myhome.home.model.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Transient;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// --- ВЛАДЕЛЬЦЫ КВАРТИР ---

@Data
@AllArgsConstructor
@Builder
public class OwnerDTO {

    private Long id;
    private String text = "";
    private String profile_picture;
    private MultipartFile profile_picture_file;

    private String first_name = "", last_name = "", fathers_name = "";
    private String fullName = "";

    private String oldpassword;
    private String phone_number = "";
    private String email = "";
    private List<BuildingDTO> buildings = new ArrayList<>();
    private List<ApartmentDTO> apartments = new ArrayList<>();
    private String apartmentNumbers = "";
    private String apartmentNames = "";
    private String date = "";
    private String status = "";
    private Boolean hasDebt = false;
    private String viber, telegram;
    private String description;
    private List<Message> messages;
    private Long unreadMessageQuantity = 0L;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;
    private String password;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime added_at;

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
    public OwnerDTO(Long id, String first_name, String last_name, String fathers_name, String fullName, List<ApartmentDTO> apartments, Long unreadMessageQuantity, String profile_picture) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.fathers_name = fathers_name;
        this.fullName = fullName;
        this.apartments = apartments;
        this.unreadMessageQuantity = unreadMessageQuantity;
        this.profile_picture = profile_picture;

    }


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
