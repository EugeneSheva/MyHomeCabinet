package com.example.myhome.home.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

// --- ДОМА ---

@Getter
@Setter
@AllArgsConstructor
public class BuildingDTO {

    private Long id;
    private String name;

    private List<String>sections  = new ArrayList<>();
    private String address;
    private List<String>floors = new ArrayList<>();


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
