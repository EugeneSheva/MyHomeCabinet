package com.example.myhome.home.model;

import lombok.Data;

import javax.persistence.*;

@Data
//@Entity
//@Table(name = "tariff_components")
@Embeddable
public class TariffComponents {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "service_id")
//    private Service service;
//
//    private Long price;
//    private String currency;
//
//    @ManyToOne
//    @JoinColumn(name = "tariff_id")
//    private Tariff tariff;

    private String service_name;
    private double price;
    private String currency;
    private String unit;

    public TariffComponents() {
    }

    public TariffComponents(String service_name, double price) {
        this.service_name = service_name;
        this.price = price;
    }
}
