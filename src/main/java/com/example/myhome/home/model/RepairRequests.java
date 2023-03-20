package com.example.myhome.home.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "repair_requests")
public class RepairRequests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RepairStatus repairStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Long phonenumber;

    @Enumerated(EnumType.STRING)
    private TypeOfRepair typeOfRepair;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

    private String description;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "master_id")
    private User master;


}
