package com.example.myhome.home.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

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

    @ToString.Exclude
    @JsonIgnore
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="apartment_id")
    private Apartment apartment;

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
}
