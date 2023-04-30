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
    private IncomeExpenseType incomeExpenseType;
    private String owner;
    private Long apartmentAccount;

    private String incomeExpenseItems;
    private String manager;
    private Double amount;
    private String description;
}
