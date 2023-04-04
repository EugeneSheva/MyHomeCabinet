package com.example.myhome.home.repository;

import com.example.myhome.home.model.CashBox;
import com.example.myhome.home.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CashBoxRepository extends JpaRepository<CashBox, Long> {
    @Query("SELECT SUM(cb.amount) FROM CashBox cb")
    Long sumAmount();
}
