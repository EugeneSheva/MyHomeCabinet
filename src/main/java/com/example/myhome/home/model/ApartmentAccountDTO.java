package com.example.myhome.home.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

// --- ЛИЦЕВЫЕ СЧЕТА ---

@Data
@AllArgsConstructor
public class ApartmentAccountDTO {
    private Long id;
    private Boolean isActive;
    private Long number;
    private Long apartment;
    private Double balance = 0.0;


}
