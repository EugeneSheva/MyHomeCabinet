package com.example.myhome.home.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

// --- ДОМА ---

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
