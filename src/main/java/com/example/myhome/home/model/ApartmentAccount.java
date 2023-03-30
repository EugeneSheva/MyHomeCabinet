package com.example.myhome.home.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

// --- ЛИЦЕВЫЕ СЧЕТА ---

@Data
@Entity
@Table(name = "apartment_account")
public class ApartmentAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isActive;

    @ToString.Exclude
    @OneToOne
    private Apartment apartment;

    private Double balance = 0.0;
}
