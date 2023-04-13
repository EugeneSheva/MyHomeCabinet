package com.example.myhome.home.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// --- КВИТАНЦИИ ---

@Data
@Entity
@Table(name = "invoices")
public class Invoice {

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
    @OneToMany(mappedBy = "invoice", cascade = {CascadeType.ALL})
    private List<InvoiceComponents> components;

    private double total_price;



    @PreRemove
    public void clearComponents() {
        this.components = null;
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
