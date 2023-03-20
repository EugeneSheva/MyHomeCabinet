package com.example.myhome.home.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "apartment_account")
public class ApartmentAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long number;
    private Boolean isActive;
    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;
    private Long balance;
}
