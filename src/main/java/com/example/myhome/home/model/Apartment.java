package com.example.myhome.home.model;

import javax.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "apartments")
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "building_id")
    private Building building;

    private Long floor;
    private Long number;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany
    @JoinColumn(name = "apartment")
    List<MeterData> meterDataList;

    @OneToMany
    @JoinColumn(name = "apartment")
    List<RepairRequests> repairRequestsList;

    @OneToMany
    @JoinColumn(name = "apartment")
    List<ApartmentAccount> apartmentAccountList;

    @OneToMany
    @JoinColumn(name = "apartment")
    List<ReceiptOfPayment> receiptOfPaymentList;

    private Double balance;
}
