package com.example.myhome.home.specification;

import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.model.Building;
import com.example.myhome.home.model.Owner;
import com.example.myhome.util.UserStatus;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class OwnerSpecification {

    public static Specification<Owner> idContains(Long id) {
        return (root, query, builder) -> builder.equal(root.get("id"), id);
    }

    public static Specification<Owner> nameContains(String name) {
        return (root, query, builder) -> {
            String[] names = name.split("\\s+");
            Predicate[] predicates = new Predicate[names.length];
            for (int i = 0; i < names.length; i++) {
                predicates[i] = builder.or(
                        builder.like(root.get("first_name"), "%" + names[i] + "%"),
                        builder.like(root.get("last_name"), "%" + names[i] + "%"),
                        builder.like(root.get("fathers_name"), "%" + names[i] + "%")
                );
            }
            return builder.and(predicates);
        };
    }

    public static Specification<Owner> phonenumberContains(String phoneNumber) {
        return (root, query, builder) -> builder.like(root.get("phone_number"), "%" + phoneNumber + "%");
    }

    public static Specification<Owner> emailContains(String email) {
        return (root, query, builder) -> builder.like(root.get("email"), "%" + email + "%");
    }

    public static Specification<Owner> buildingContains(String name) {
        return (root, query, builder) -> {
            Join<Owner, Apartment> ownerJoin = root.join("apartments", JoinType.INNER);
            Join<Owner, Building> thirdTableJoin = ownerJoin.join("building", JoinType.INNER);
            return builder.like(thirdTableJoin.get("name"), "%" + name + "%");
        };
    }

    public static Specification<Owner> apartmentContains(Long number) {
        return (root, query, builder) -> {
            Join<Owner, Apartment> apartmentJoin = root.join("apartments", JoinType.INNER);
            return builder.equal(apartmentJoin.get("id"), number);
        };
    }
    public static Specification<Owner> dateContains(LocalDate date) {
        return (root, query, builder) -> {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
            return builder.between(root.get("added_at"), startOfDay, endOfDay);
        };
    }
    public static Specification<Owner> statusContains(UserStatus userStatus) {
        return (root, query, builder) -> builder.equal(root.get("status"), userStatus);
    }
    public static Specification<Owner> hasDebtContains() {
        return (root, query, builder) -> builder.equal(root.get("has_debt"), true);
    }
    public static Specification<Owner> noDebtContains() {
        return (root, query, builder) -> builder.equal(root.get("has_debt"), false);
    }

}
