package com.example.myhome.home.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

// --- КВИТАНЦИИ ---

@Data
@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //может начинаться с нулей - 04122200356 , через String.format надо будет как-то учитывать
    private Long number;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

    private Boolean completed;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;

    @OneToMany(mappedBy = "invoice")
    private List<InvoiceComponents> components;

}
