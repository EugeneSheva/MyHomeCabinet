package com.example.myhome.home.specification;


import com.example.myhome.home.model.Building;
import org.springframework.data.jpa.domain.Specification;


public class BuildingSpecification {


    public static Specification<Building> addressContains(String address) {
        return (root, query, builder) -> builder.like(root.get("address"), "%" + address + "%");
    }

    public static Specification<Building> nameContains(String name) {
        return (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
    }

}
