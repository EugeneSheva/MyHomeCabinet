package com.example.myhome.home.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MeterDataDTO {

    private Long id;

    private Long buildingID;
    private String buildingName;

    private String section;

    private Long apartmentID;
    private Long apartmentNumber;

    private Long serviceID;
    private String serviceName;
    private String serviceUnitName;

    private Double readings;

}
