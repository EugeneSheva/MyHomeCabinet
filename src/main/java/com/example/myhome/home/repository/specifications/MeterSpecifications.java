package com.example.myhome.home.repository.specifications;

import com.example.myhome.home.model.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class MeterSpecifications {

    public static Specification<MeterData> hasBuilding(Building building) {
        if(building == null) return (root, query, criteriaBuilder) -> null;
        else return (root, query, cb) -> {
            Join<Apartment, MeterData> meterApartment = root.join("apartment");
            return cb.equal(meterApartment.get(Apartment_.BUILDING), building);
        };
    }

//    public static Specification<MeterData> hasBuilding(Building building) {
//        if(building == null) return (root, query, criteriaBuilder) -> null;
//        else return (root, query, cb) -> {
//            Predicate predicate = cb.conjunction();
//            Join<Apartment, MeterData> meterApartment = root.join("apartment");
//            predicate.getExpressions().add(cb.equal(meterApartment.get(Apartment_.BUILDING), building));
//            query.groupBy(root.get(MeterData_.APARTMENT), root.get(MeterData_.SERVICE));
//            return predicate;
//        };
//    }

    public static Specification<MeterData> hasSection(String section) {
        if(section == null) return (root, query, criteriaBuilder) -> null;
        else return (root, query, cb) -> {
            Join<Apartment, MeterData> meterApartment = root.join("apartment");
            return cb.equal(meterApartment.get(Apartment_.SECTION), section);
        };
    }

    public static Specification<MeterData> hasApartment(Apartment apartment) {
        if(apartment == null) return (root, query, criteriaBuilder) -> null;
        else return (root, query, cb) -> cb.equal(root.get(MeterData_.APARTMENT), apartment);
    }

    public static Specification<MeterData> hasService(Service service) {
        if(service == null) return (root, query, criteriaBuilder) -> null;
        else return (root, query, cb) -> cb.equal(root.get(MeterData_.SERVICE), service);
    }

}
