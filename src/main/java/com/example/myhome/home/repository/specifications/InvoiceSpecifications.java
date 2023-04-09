package com.example.myhome.home.repository.specifications;

import com.example.myhome.home.model.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import java.time.LocalDate;

public class InvoiceSpecifications {

    public static Specification<Invoice> hasId(Long id) {
        if(id == null) return (root, query, criteriaBuilder) -> null;
        return (root, query, cb) -> cb.equal(root.get(Invoice_.ID), id);
    }

    public static Specification<Invoice> hasStatus(InvoiceStatus status) {
        if(status == null) return (root, query, criteriaBuilder) -> null;
        return (root, query, cb) -> cb.equal(root.get(Invoice_.STATUS), status);
    }

    public static Specification<Invoice> hasApartment(Apartment apartment) {
        if(apartment == null) return (root, query, criteriaBuilder) -> null;
        return (root, query, cb) -> cb.equal(root.get(Invoice_.APARTMENT), apartment);
    }

    public static Specification<Invoice> hasOwner(Owner owner) {
        if(owner == null) return  (root, query, criteriaBuilder) -> null;
        return (root, query, cb) -> {
            Join<Apartment, Invoice> invoiceApartment = root.join("apartment");
            return cb.equal(invoiceApartment.get(Apartment_.OWNER), owner);
        };
    }

    public static Specification<Invoice> isCompleted(Boolean completed) {
        if(completed == null) return (root, query, criteriaBuilder) -> null;
        return (root, query, cb) -> cb.equal(root.get(Invoice_.COMPLETED), completed);
    }

    public static Specification<Invoice> datesBetween(LocalDate from, LocalDate to) {
        if(from == null || to == null) return (root, query, criteriaBuilder) -> null;
        return (root, query, cb) -> cb.between(root.get(Invoice_.DATE), from, to);
    }

}
