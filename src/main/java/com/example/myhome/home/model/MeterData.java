package com.example.myhome.home.model;

import javax.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDate;
import java.util.Date;

// --- ПОКАЗАНИЯ СЧЕТЧИКОВ ---

@Data
@Entity
@Table(name = "meters_data")
public class MeterData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

    private double currentReadings;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;




}
