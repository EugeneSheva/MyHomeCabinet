package com.example.myhome.home.model;

import javax.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;


import java.util.Date;
import java.util.List;


@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long phonenumber;
    private String email;

    @OneToMany
    @JoinColumn(name = "user")
    private List <Apartment>appartments;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfRegistry;

    private String password;
    private boolean active;
    private String role;

    @OneToMany
    @JoinColumn(name = "user")
    private List <RepairRequests>repairRequestsList;

    @ManyToMany(mappedBy = "receiverList")
    private List<Message>messageList;

    @OneToMany
    @JoinColumn(name = "owner")
    private List <CashBox>cashBoxList;

    @OneToMany
    @JoinColumn(name = "manager")
    private List <CashBox>cashBoxListManager;




}

