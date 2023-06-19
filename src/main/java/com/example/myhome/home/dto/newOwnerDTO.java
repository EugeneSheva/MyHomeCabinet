package com.example.myhome.home.dto;


import com.example.myhome.home.model.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// --- ВЛАДЕЛЬЦЫ КВАРТИР ---

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class newOwnerDTO {

    private String first_name = "", last_name = "", fathers_name = "";
    private String email = "";
    private String password;
    private String repassword;
    private Boolean confirm;

}
