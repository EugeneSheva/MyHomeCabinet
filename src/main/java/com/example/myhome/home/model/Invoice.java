package com.example.myhome.home.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// --- КВИТАНЦИИ ---

@Data
@Entity
@Table(name = "invoices")
public class Invoice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

    @ManyToOne
    @JoinColumn(name = "building_id")
    private Building building;

    private String section;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "account_id")
    private ApartmentAccount account;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="tariff_id")
    private Tariff tariff;

    private Boolean completed;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "invoice")
    private List<InvoiceComponents> components = new ArrayList<>();

    private double total_price;

    public Invoice(long id, LocalDate localDate, Apartment apartment, double v, LocalDate dateFrom, LocalDate dateTo) {
        this.id = id;
        this.date = localDate;
        this.apartment = apartment;
        this.total_price = v;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public Invoice() {

    }

    public void addComponent(InvoiceComponents component) {
        component.setInvoice(this);
        components.add(component);
    }

    public void removeComponent(InvoiceComponents component) {
        components.remove(component);
        component.setInvoice(null);
    }

    public void removeAllChildren() {
        for (int i = 0; i < components.size(); i++) {
            InvoiceComponents child = components.get(i);
            this.removeComponent(child);
        }
    }

}
