package com.example.myhome.home.model;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Invoice.class)
public abstract class Invoice_ {
    public static volatile SingularAttribute<Invoice, Long> id;
    public static volatile SingularAttribute<Invoice, LocalDate> date;
    public static volatile SingularAttribute<Invoice, Apartment> apartment;
    public static volatile SingularAttribute<Invoice, Tariff> tariff;
    public static volatile SingularAttribute<Invoice, Boolean> completed;
    public static volatile SingularAttribute<Invoice, InvoiceStatus> status;
    public static volatile SingularAttribute<Invoice, LocalDate> dateFrom;
    public static volatile SingularAttribute<Invoice, LocalDate> dateTo;
    public static volatile SingularAttribute<Invoice, Double> totalPrice;
    public static volatile ListAttribute<Invoice, InvoiceComponents> components;

}
