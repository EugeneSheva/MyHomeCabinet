package com.example.myhome.home.model;

import com.example.myhome.util.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

// --- ПОЛЬЗОВАТЕЛИ ---

@Data
@AllArgsConstructor
public class AdminDTO {

    private Long id;
    private String first_name, last_name;

    private String text;

    private String phone_number;
    private String email;
    private boolean active = true;
    private UserRole role;

    public AdminDTO() {
    }

    public AdminDTO(Long id, String first_name, String last_name) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.text = getFullName() + "(ID:"+this.id+")";
    }

    public AdminDTO(Long id, String first_name, String last_name, String phone_number, String email, boolean active, UserRole role) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_number = phone_number;
        this.email = email;
        this.active = active;
        this.role = role;
    }

    public String getFullName() {
        return this.first_name + ' ' + this.last_name;
    }



}

