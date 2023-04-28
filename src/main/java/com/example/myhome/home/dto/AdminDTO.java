package com.example.myhome.home.dto;

import com.example.myhome.util.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

// --- ПОЛЬЗОВАТЕЛИ ---

@Data
@AllArgsConstructor
@Builder
public class AdminDTO {

    private Long id;
    private String text;

    @NotBlank(message = "Поле не должно быть пустым!")
    private String first_name, last_name;

    @NotBlank(message = "Укажите номер телефона!")
    private String phone_number;

    @NotBlank(message = "Укажите почту!")
    @Email(message = "Неправильный формат электронной почты")
    private String email;

    private String password;

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

