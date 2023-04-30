package com.example.myhome.home.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.example.myhome.util.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDate;
import java.util.List;

// --- ПОЛЬЗОВАТЕЛИ ---

@Data
@Entity
@Table(name = "admins")
public class Admin {

    /*
    пользователи-админы системы
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Поле не должно быть пустым!")
    private String first_name, last_name;
    @NotBlank(message = "Укажите номер телефона!")
    private String phone_number;
    @NotBlank(message = "Укажите почту!")
    @Email(message = "Неправильный формат электронной почты")
    private String email;
    @NotBlank(message = "Необходимо написать пароль!")
    private String password;
    private boolean active = true;

    private String full_name = first_name + " " + last_name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfRegistry;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @JsonIgnore
    @ManyToMany(mappedBy = "receivers")
    private List<Message> messageList;
    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "ownerId")
    private List<CashBox> cashBoxList;
    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "manager")
    private List<CashBox> cashBoxListManager;
    @JsonIgnore
    @ManyToMany(mappedBy = "admins")
    List<Building> buildings;



    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", name='" + first_name+ " " + last_name + '\'' +
                ", phone_number=" + phone_number +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", active=" + active +
                ", dateOfRegistry=" + dateOfRegistry +
                ", role=" + role +
                "}\n";
    }

    public String getFullName() {
        return this.first_name + ' ' + this.last_name;
    }
}

