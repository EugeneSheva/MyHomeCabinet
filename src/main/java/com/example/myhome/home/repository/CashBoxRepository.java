package com.example.myhome.home.repository;

import com.example.myhome.home.model.CashBox;
import com.example.myhome.home.model.IncomeExpenseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CashBoxRepository extends JpaRepository<CashBox, Long> {
    @Query("SELECT SUM(cb.amount) FROM CashBox cb WHERE cb.completed=true")
    Double sumAmount();

    @Query("SELECT SUM(cb.amount) FROM CashBox cb WHERE MONTH(cb.date) = :month AND YEAR(cb.date) = :year AND (cb.incomeExpenseType) = :incomeExpenseType AND cb.completed=true")
    Double getSumByMonth(@Param("month") int month, @Param("year") int year, @Param("incomeExpenseType") IncomeExpenseType incomeExpenseType);




    }
