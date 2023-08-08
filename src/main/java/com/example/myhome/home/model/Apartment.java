package com.example.myhome.home.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "apartments")
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinTable(name="building_apartments",
            joinColumns = @JoinColumn(name="apartment_id"),
            inverseJoinColumns = @JoinColumn(name="building_id"))
    private Building building;

    private String section;
    private String floor;
    private Long number;

    private Double balance;

    private Double square;

    //В примере за каждой квартирой ставится только один лицевой счет
    @OneToOne(mappedBy = "apartment")
    private ApartmentAccount account;

    @ManyToOne
    @JoinTable(name="apartment_owners",
            joinColumns = @JoinColumn(name="apartment_id"),
            inverseJoinColumns = @JoinColumn(name="owner_id"))
    private Owner owner;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;

    //Показания счётчиков
    @JsonIgnore
    @OneToMany(mappedBy = "apartment")
    private List<MeterData> meterDataList;

    @JsonIgnore
    @OneToMany(mappedBy = "apartment")
    private List<RepairRequest> repairRequestsList;

    //Квитанции
    @JsonIgnore
    @OneToMany(mappedBy = "apartment")
    private List<Invoice> invoiceList;

    public Apartment() {
    }

    public Apartment(Long id, Building building, String section, String floor, Long number, Double balance, Double square, Owner owner) {
        this.id = id;
        this.building = building;
        this.section = section;
        this.floor = floor;
        this.number = number;
        this.balance = balance;
        this.square = square;
        this.owner = owner;
    }

    public Apartment(Long id, Long numb) {

        this.id = id;
        this.number = numb;
    }

    public Apartment(long l, long l1, Building test, Owner owner) {
        this.id = l;
        this.number = l1;
        this.building = test;
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Apartment{" +
                "id=" + id +
                ", section=" + section +
                ", floor=" + floor +
                ", number=" + number +
                ", balance=" + balance +
                "}\n";
    }
}