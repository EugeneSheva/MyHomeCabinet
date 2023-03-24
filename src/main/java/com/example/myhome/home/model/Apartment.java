package com.example.myhome.home.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "apartments")
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="building_id")
    private Building building;

    private String section;
    private String floor;
    private Long number;

    private Double balance;

    private Double square;

    //В примере за каждой квартирой ставится только один лицевой счет
    @OneToOne
    private ApartmentAccount account;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Owner owner;

    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;

    //Показания счётчиков
    @OneToMany(mappedBy = "apartment")
    private List<MeterData> meterDataList;

    @OneToMany(mappedBy = "apartment")
    private List<RepairRequests> repairRequestsList;

    //Квитанции
    @OneToMany(mappedBy = "apartment")
    private List<Invoice> invoiceList;

    @Override
    public String toString() {
        return "Apartment{" +
                "id=" + id +
                ", section=" + section +
                ", floor=" + floor +
                ", number=" + number +
                ", owner=" + owner +
                ", balance=" + balance +
                "}\n";
    }
}
