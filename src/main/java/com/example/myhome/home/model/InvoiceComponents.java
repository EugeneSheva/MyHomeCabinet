package com.example.myhome.home.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "invoice_components")
public class InvoiceComponents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    private double unit_price;
    private double unit_amount;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    public double getTotalPrice() {return this.unit_price*this.unit_amount;}


}