package com.example.myhome.home.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class CashBoxDTO {

    private Long id;
    private LocalDate date;
    private Boolean completed;
    private String transactionItemName;

    private String ownerFullName;
    private String accountNumber;

    private String transactionType;
    private Double amount;

}
