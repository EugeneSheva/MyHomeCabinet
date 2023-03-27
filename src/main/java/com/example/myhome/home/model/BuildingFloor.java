package com.example.myhome.home.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "building_floors")
public class BuildingFloor {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="building_id")
    private Building building;

    @OneToMany(mappedBy = "floor")
    private List<Apartment> apartments;

    private String name;

    public BuildingFloor() {
    }

    public BuildingFloor(Long id, Building building, String name) {
        this.id = id;
        this.building = building;
        this.name = name;
    }

    @Override
    public String toString() {
        return "BuildingFloor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
