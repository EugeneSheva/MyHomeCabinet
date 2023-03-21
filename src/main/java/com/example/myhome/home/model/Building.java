package com.example.myhome.home.model;

import javax.persistence.*;
import lombok.Data;

import java.util.List;

// --- ДОМА ---

@Data
@Entity
@Table(name = "buildings")
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ElementCollection
    private List<String>sections;
    private String address;
    @ElementCollection
    private List<String>floors;

    @OneToMany(mappedBy = "building")
    List<Apartment> apartments;

    @ManyToMany
    @JoinTable(name="building_admins",
            joinColumns = @JoinColumn(name="building_id"),
            inverseJoinColumns = @JoinColumn(name="admin_id"))
    List<Admin> admins;

    @Override
    public String toString() {
        return "Building{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sections=" + sections +
                ", address='" + address + '\'' +
                ", floors=" + floors +
                '}';
    }
}
