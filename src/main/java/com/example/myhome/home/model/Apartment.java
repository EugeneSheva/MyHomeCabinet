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
    @ManyToOne
    @JoinTable(name="building_apartments",
            joinColumns = @JoinColumn(name="apartment_id"),
            inverseJoinColumns = @JoinColumn(name="building_id"))
    private Building building;

    @JsonIgnore
    @ManyToOne
    @JoinTable(name="building_section_apartments",
            joinColumns = @JoinColumn(name="apartment_id"),
            inverseJoinColumns = @JoinColumn(name="building_section_id"))
    private BuildingSection section;

    @JsonIgnore
    @ManyToOne
    @JoinTable(name="building_floor_apartments",
            joinColumns = @JoinColumn(name="apartment_id"),
            inverseJoinColumns = @JoinColumn(name="building_floor_id"))
    private BuildingFloor floor;

    private Long number;

    private Double balance;

    private Double square;

    //В примере за каждой квартирой ставится только один лицевой счет
    @OneToOne
    private ApartmentAccount account;

    @ManyToOne
    @JoinTable(name="apartment_owners",
            joinColumns = @JoinColumn(name="apartment_id"),
            inverseJoinColumns = @JoinColumn(name="owner_id"))
    private Owner owner;

    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;

    //Показания счётчиков
    @OneToMany(mappedBy = "apartment")
    private List<MeterData> meterDataList;

    @OneToMany(mappedBy = "apartment")
    private List<RepairRequest> repairRequestsList;

    //Квитанции
    @OneToMany(mappedBy = "apartment")
    private List<Invoice> invoiceList;

    public Apartment() {
    }

    public Apartment(Long id, Building building, BuildingSection section, BuildingFloor floor, Long number, Double balance, Double square, Owner owner) {
        this.id = id;
        this.building = building;
        this.section = section;
        this.floor = floor;
        this.number = number;
        this.balance = balance;
        this.square = square;
        this.owner = owner;
    }

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