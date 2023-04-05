package com.example.myhome.home.model;

import com.example.myhome.util.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// --- ВЛАДЕЛЬЦЫ КВАРТИР ---

@Data
@AllArgsConstructor
public class OwnerDTO {

    private Long id;

    private String first_name, last_name, fathers_name;

    public String getFullName() {
        return this.first_name + ' ' + this.fathers_name + ' ' + this.last_name;
    }


}
