package com.example.myhome.home.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

// --- ЛИЦЕВЫЕ СЧЕТА ---

@Data
@Entity
@Table(name = "apartment_account")
public class ApartmentAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long number;

    private Boolean isActive;

    @Transient
    private Boolean changedState;

    @ToString.Exclude
    @JsonIgnore
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="apartment_id")
    private Apartment apartment;

    @ManyToOne
    @JoinColumn(name="building_id")
    private Building building;

    @ManyToOne
    @JoinColumn(name="owner_id")
    private Owner owner;

    private String section;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "apartmentAccount")
    private List<CashBox> transactions = new ArrayList<>();

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
    private List<Invoice> invoices = new ArrayList<>();

    private Double balance = 0.0;

    public ApartmentAccount() {
    }

    public ApartmentAccount(Long id, Boolean isActive, Double balance) {
        this.id = id;
        this.isActive = isActive;
        this.balance = balance;
    }

    @PreRemove
    public void removeApartmentLink() {
        this.apartment.setAccount(null);
    }

    public double getAccountBalance() {
        return
                this.transactions.stream()
                .filter(CashBox::getCompleted)
                .map(CashBox::getAmount)
                .reduce(Double::sum)
                .orElse(0.0)

                - this.apartment.getInvoiceList().stream()
                .filter(Invoice::getCompleted)
                .map(Invoice::getTotal_price)
                .reduce(Double::sum)
                .orElse(0.0);
    }

    public void addToBalance(double amount) {
        this.balance += amount;
    }

    public Long getId() {
        return id;
    }
}
