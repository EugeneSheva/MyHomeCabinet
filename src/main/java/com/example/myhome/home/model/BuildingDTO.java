package com.example.myhome.home.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

// --- ДОМА ---

@Data
public class BuildingDTO {

    private Long id;
    private String name;
    private String text;

    private List<String>sections  = new ArrayList<>();
    private String address;
    private List<String>floors = new ArrayList<>();

    public BuildingDTO() {
    }

    public BuildingDTO(Long id, String name) {
        this.id = id;
        this.name = name;
        this.text = name;
    }

    public BuildingDTO(Long id, String name, List<String> sections, String address, List<String> floors) {
        this.id = id;
        this.name = name;
        this.sections = sections;
        this.address = address;
        this.floors = floors;
    }

    @Override
    public String toString() {
        return "BuildingDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
//                ", sections=" + sections +
                ", address='" + address + '\'' +
//                ", floors=" + floors +
                '}';
    }

}
