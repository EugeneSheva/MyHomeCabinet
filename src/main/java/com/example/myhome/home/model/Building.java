package com.example.myhome.home.model;

import javax.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "buildings")
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long section;
    private String address;
    private Long floors;
    @OneToMany
    @JoinColumn(name = "building")
    List<Apartment> apartments;
}
