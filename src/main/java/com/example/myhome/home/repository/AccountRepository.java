package com.example.myhome.home.repository;

import com.example.myhome.home.model.ApartmentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<ApartmentAccount, Long> {
    Long countAllBy();

    Optional<ApartmentAccount> findFirstByOrderByIdDesc();

    Optional<ApartmentAccount> findByApartmentId(long apartment_id);

    @Query("SELECT SUM(aa.balance) FROM ApartmentAccount aa")
    Long getBalance();

    @Query("SELECT SUM(aa.balance) FROM ApartmentAccount aa WHERE aa.balance<0")
    Long getDebt();
}
