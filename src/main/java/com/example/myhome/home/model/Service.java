package com.example.myhome.home.model;

import lombok.Data;

import javax.persistence.*;

// --- УСЛУГИ ---

@Data
@Entity
@Table(name = "services")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    //флажок "показывать в счётчиках"
    private boolean show_in_meters;

    @ManyToOne
    @JoinColumn(name = "unit_ID")
    private Unit unit;

}
