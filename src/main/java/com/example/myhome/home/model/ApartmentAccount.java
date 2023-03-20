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

    //может начинаться с нулей - 04122200356 , через String.format надо будет как-то учитывать
    private Long number;

    private Boolean isActive;

    @ToString.Exclude
    @OneToOne
    private Apartment apartment;

    private Long balance;
}
