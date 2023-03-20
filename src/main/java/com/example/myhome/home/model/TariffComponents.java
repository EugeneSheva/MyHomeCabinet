package com.example.myhome.home.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tariff_components")
public class TariffComponents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;
    private Long price;
    private String currency;
    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;

}
