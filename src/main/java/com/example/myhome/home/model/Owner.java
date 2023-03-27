package com.example.myhome.home.model;

import com.example.myhome.util.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// --- ВЛАДЕЛЬЦЫ КВАРТИР ---

@Data
@Entity
@Table(name="owners")
public class Owner {

    /*
    пользователи-владельцы квартир
    Есть смысл сделать их отдельно от админов, потому что в примере сайта логин делится на две части:
    1. Для жильца(владельца квартиры) - вход в личный кабинет
    2. Для администрации(админа системы) - вход в админ панель
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //ФИО
    private String first_name, last_name, fathers_name;

    private String phone_number;

    private String email, password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime added_at;

    private String viber, telegram;

    @JsonIgnore
    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<Apartment> apartments;

    @ManyToMany(mappedBy = "receivers")
    private List<Message> messages;

    private boolean has_debt;

    private String description;
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    //аватарка
    private String profile_picture;

    public Owner() {
    }

    public Owner(Long id, String first_name, String last_name, String fathers_name, String phone_number, String email, String password) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.fathers_name = fathers_name;
        this.phone_number = phone_number;
        this.email = email;
        this.password = password;
    }
}