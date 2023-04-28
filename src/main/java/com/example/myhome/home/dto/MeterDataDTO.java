package com.example.myhome.home.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeterDataDTO {

    private Long id;

    private String status;
    private LocalDate date;

    private Long buildingID;
    private String buildingName;

    private String section;

    private Long apartmentID;
    private Long apartmentNumber;

    private Long serviceID;
    private String serviceName;
    private String serviceUnitName;

    private Long apartmentOwnerID;
    private String apartmentOwnerFullName;

    private Double readings;

}
