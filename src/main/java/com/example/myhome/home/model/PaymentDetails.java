package com.example.myhome.home.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "payment_details")
public class PaymentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
}
