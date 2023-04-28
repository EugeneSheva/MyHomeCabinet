package com.example.myhome.home.specification;

import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.model.Building;
import com.example.myhome.home.model.Message;
import com.example.myhome.home.model.Owner;
import com.example.myhome.util.UserStatus;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class MessageSpecification {


    public static Specification<Message> textContains(String text) {
        if(text == null) return (root, query, criteriaBuilder) -> null;
        return (root, query, builder) -> {
            Predicate[] predicates = new Predicate[2];
            for (int i = 0; i < 2; i++) {
                predicates[i] = builder.or(
                        builder.like(root.get("subject"), "%" + text + "%"),
                        builder.like(root.get("text"), "%" + text + "%")
                );
            }
            return builder.and(predicates);
        };
    }

}
