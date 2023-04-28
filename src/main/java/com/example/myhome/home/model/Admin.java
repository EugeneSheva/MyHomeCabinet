package com.example.myhome.home.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.example.myhome.util.UserRole;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDate;
import java.util.List;

// --- ПОЛЬЗОВАТЕЛИ ---

@Data
@Entity
@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String first_name, last_name;
    private String phone_number;

    private String email;

    private String password;

    private boolean active = true;

    private String full_name = first_name + " " + last_name;

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

    @ManyToMany(mappedBy = "admins")
    List<Building> buildings;

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", name='" + first_name+ " " + last_name + '\'' +
                ", phone_number=" + phone_number +
                ", email='" + email + '\'' +
                ", active=" + active +
                ", dateOfRegistry=" + dateOfRegistry +
                ", role=" + role +
                "}\n";
    }

    public String getFullName() {
        return this.first_name + ' ' + this.last_name;
    }
}

