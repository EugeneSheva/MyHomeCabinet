package com.example.myhome.home.model;

import javax.persistence.*;

import com.example.myhome.util.UserRole;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;

// --- ПОЛЬЗОВАТЕЛИ ---

@Data
@Entity
@Table(name = "users")
public class User {

    /*
    пользователи-админы системы
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long phone_number;
    private String email;
    private String password;
    private boolean active;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfRegistry;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ManyToMany(mappedBy = "receivers")
    private List<Message> messageList;

    @OneToMany
    @JoinColumn(name = "owner")
    private List<CashBox> cashBoxList;

    @OneToMany
    @JoinColumn(name = "manager")
    private List<CashBox> cashBoxListManager;

    @ManyToMany(mappedBy = "users")
    List<Building> buildings;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone_number=" + phone_number +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", active=" + active +
                ", dateOfRegistry=" + dateOfRegistry +
                ", role=" + role +
                "}\n";
    }
}

