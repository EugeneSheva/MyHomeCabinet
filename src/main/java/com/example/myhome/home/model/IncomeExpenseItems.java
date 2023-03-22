package com.example.myhome.home.model;

import lombok.Data;

import javax.persistence.*;

// --- СТАТЬИ ПРИХОДОВ/РАСХОДОВ ---

@Data
@Entity
@Table(name = "income_expense_items")
public class IncomeExpenseItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private IncomeExpenseType incomeExpenseType;

    public IncomeExpenseItems() {
    }

    public IncomeExpenseItems(String name, IncomeExpenseType incomeExpenseType) {
        this.name = name;
        this.incomeExpenseType = incomeExpenseType;
    }
}
