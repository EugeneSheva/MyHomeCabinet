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

    public InvoiceComponents(long l, Invoice invoice, Service service1, double p, double a) {
        this.id = l;
        this.invoice = invoice;
        this.service = service1;
        this.unit_price = p;
        this.unit_amount = a;
    }

    public InvoiceComponents() {

    }

    @Override
    public String toString() {
        return "InvoiceComponents{" +
                "id=" + id +
                ", service=" + service +
                ", unit_price=" + unit_price +
                ", unit_amount=" + unit_amount +
                '}';
    }

    public double getTotalPrice() {return this.unit_price*this.unit_amount;}

}