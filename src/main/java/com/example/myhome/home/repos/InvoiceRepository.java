package com.example.myhome.home.repos;

import com.example.myhome.home.model.ApartmentAccount;
import com.example.myhome.home.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query("SELECT MAX(i.id) FROM Invoice i")
    Optional<Long> getMaxId();

}
