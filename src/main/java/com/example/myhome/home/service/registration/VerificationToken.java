package com.example.myhome.home.service.registration;

import com.example.myhome.home.model.Owner;
import lombok.Data;

import javax.persistence.*;
import java.security.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Data
@Entity
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @ManyToOne
    @JoinColumn(nullable = false, name = "owner_id")
    private Owner owner;

    private LocalDate createdAt;
    private LocalDate expiresAt;

    public VerificationToken() {
    }

    public VerificationToken(String token, Owner owner) {
        this.token = token;
        this.owner = owner;
        this.createdAt = LocalDate.now();
        this.expiresAt = LocalDate.now().plusDays(1);
    }

    public boolean isValid() {
        return (LocalDate.now().isBefore(this.expiresAt));
    }
}
