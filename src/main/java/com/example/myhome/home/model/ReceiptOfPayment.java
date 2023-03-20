package com.example.myhome.home.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "receipt_of_payment")
public class ReceiptOfPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long number;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date receiptDate;

    private Boolean carriedOut;
    @Enumerated(EnumType.STRING)
    private PaymantStatus paymantStatus;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateFrom;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateTo;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "receiptOfPayment")
    private List<ReceiptOfPaymentComponents> receiptOfPaymentComponentsList = new java.util.ArrayList<>();




}
