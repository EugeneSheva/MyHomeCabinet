package com.example.myhome.home.specification;

import com.example.myhome.home.model.*;
import com.example.myhome.util.UserStatus;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class CashBoxSpecification {

    public static Specification<CashBox> hasId(Long id) {
        if(id == null) return (root, query, criteriaBuilder) -> null;
        return (root, query, builder) -> builder.equal(root.get("id"), id);
    }

    public static Specification<CashBox> dateBetwenContains(LocalDate startOfDay, LocalDate endOfDay) {
        return (root, query, builder) ->  builder.between(root.get("date"), startOfDay, endOfDay);
    }
    public static Specification<CashBox> dateAfterContains(LocalDate date) {
        return (root, query, builder) ->  builder.greaterThanOrEqualTo(root.get("date"), date);
    }
    public static Specification<CashBox> dateBeforeContains(LocalDate date) {
        return (root, query, builder) ->  builder.lessThanOrEqualTo(root.get("date"), date);
    }

    public static Specification<CashBox> hasDatesBetween(LocalDate date_from, LocalDate date_to) {
        if(date_to == null || date_from == null) return (root, query, criteriaBuilder) -> null;
        return (root, query, cb) -> cb.between(root.get(CashBox_.DATE), date_from, date_to);
    }

    public static Specification<CashBox> isCompletedContains(Boolean isComplete) {
        if(isComplete == null) return (root, query, criteriaBuilder) -> null;
        return (root, query, builder) -> builder.equal(root.get(CashBox_.COMPLETED), isComplete);
    }

    public static Specification<CashBox> hasTransactionItemID(Long itemID) {
        if(itemID == null) return (root, query, criteriaBuilder) -> null;
        return (root, query, builder) -> {
            Join<CashBox, IncomeExpenseItems> itemJoin = root.join(CashBox_.INCOME_EXPENSE_ITEMS, JoinType.INNER);
            return builder.equal(itemJoin.get(IncomeExpenseItems_.ID), itemID);
        };
    }

    public static Specification<CashBox> hasOwnerID(Long ownerID) {
        if(ownerID == null) return (root, query, criteriaBuilder) -> null;
        return (root, query, cb) -> {
            Join<CashBox, Owner> ownerJoin = root.join(CashBox_.OWNER);
            return cb.equal(ownerJoin.get(Owner_.ID), ownerID);
        };
    }

    public static Specification<CashBox> hasAccountID(Long accountID) {
        if(accountID == null) return (root, query, criteriaBuilder) -> null;
        return (root, query, cb) -> {
            Join<CashBox, ApartmentAccount> accountJoin = root.join(CashBox_.APARTMENT_ACCOUNT);
            return cb.equal(accountJoin.get(ApartmentAccount_.ID), accountID);
        };
    }

    public static Specification<CashBox> hasTransactionType(IncomeExpenseType type) {
        if(type == null) return (root, query, criteriaBuilder) -> null;
        return (root, query, cb) -> cb.equal(root.get(CashBox_.INCOME_EXPENSE_TYPE), type);
    }

    public static Specification<CashBox> incExpItemContains(String incExpItem) {
        return (root, query, builder) -> {
            Join<CashBox, IncomeExpenseItems> Join = root.join("incomeExpenseItems", JoinType.INNER);
            return builder.equal(Join.get("name"), incExpItem);
        };
    }

    public static Specification<CashBox> ownerContains(Long userId) {
        return (root, query, builder) -> {
            Join<CashBox, Owner> cashBoxOwnerJoin = root.join("owner", JoinType.INNER);
            return builder.equal(cashBoxOwnerJoin.get("id"), userId);
        };
    }

    public static Specification<CashBox> accountContains(Long acc) {
        return (root, query, builder) -> {
            Join<CashBox, ApartmentAccount> cashBoxOwnerJoin = root.join("apartmentAccount", JoinType.INNER);
            return builder.equal(cashBoxOwnerJoin.get("id"), acc);
        };
    }

    public static Specification<CashBox> incomeExpenseTypeContains(IncomeExpenseType incomeExpenseType) {
        return (root, query, builder) -> builder.equal(root.get("incomeExpenseType"), incomeExpenseType);
    }

}
