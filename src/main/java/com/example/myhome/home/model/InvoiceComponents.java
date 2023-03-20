package com.example.myhome.home.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "receipt_of_payment_components")
public class InvoiceComponents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;

    //насчет one to one хз
    @OneToOne
    @JoinColumn(name = "meter_data_id")
    private MeterData meterData;

    @ManyToOne
    @JoinColumn(name = "receipt_of_payment_id")
    private Invoice invoice;


}