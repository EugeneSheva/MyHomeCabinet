package com.example.myhome.home.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class RepairRequestDTO {

    private Long id;
    private String best_time;
    private String master_type;
    private String description;

    private Long apartmentID;
    private Long apartmentNumber;
    private String apartmentBuildingName;

    private Long ownerID;
    private String ownerFullName;
    private String ownerPhoneNumber;

    private Long masterID;
    private String masterFullName;

    private String status;

}
