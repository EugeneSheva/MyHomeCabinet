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
    private String phone_number;
    private String email;
    private boolean active = true;
    private UserRole role;



}

