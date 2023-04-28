package com.example.myhome.home.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// --- КВИТАНЦИИ ---

@Data
@AllArgsConstructor
public class InvoiceDTO {
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private ApartmentDTO apartment;
    private BuildingDTO building;
    private ApartmentAccountDTO account;
    private OwnerDTO owner;
    private Boolean completed;
    private InvoiceStatus status;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;
    private double total_price;

    private List<InvoiceComponents> components = new ArrayList<>();
    private Tariff tariff;



}
