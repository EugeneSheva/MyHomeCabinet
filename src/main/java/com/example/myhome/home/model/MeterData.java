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

    @ManyToOne
    @JoinColumn(name = "building_id")
    private Building building;

    private String section;

    private double currentReadings;

    @Enumerated(EnumType.STRING)
    private MeterPaymentStatus status;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date = LocalDate.now();


}
