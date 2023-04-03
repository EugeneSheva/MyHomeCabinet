package com.example.myhome.home.repos;

import com.example.myhome.home.model.CashBox;
import com.example.myhome.home.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashBoxRepository extends JpaRepository<CashBox, Long> {
}
