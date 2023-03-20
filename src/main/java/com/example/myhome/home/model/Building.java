package com.example.myhome.home.model;

import javax.persistence.*;
import lombok.Data;
import lombok.ToString;

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
    private Long section;
    private String address;
    private Long floors;

    @OneToMany(mappedBy = "building")
    List<Apartment> apartments;

    @ManyToMany
    @JoinTable(name="building_admins",
            joinColumns = @JoinColumn(name="building_id"),
            inverseJoinColumns = @JoinColumn(name="admin_id"))
    List<User> users;

    @Override
    public String toString() {
        return "Building{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", section=" + section +
                ", address='" + address + '\'' +
                ", floors=" + floors +
                '}';
    }
}
