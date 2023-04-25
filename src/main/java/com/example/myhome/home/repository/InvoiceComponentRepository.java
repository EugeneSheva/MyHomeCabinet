package com.example.myhome.home.repository;

import com.example.myhome.home.model.InvoiceComponents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvoiceComponentRepository extends JpaRepository<InvoiceComponents, Long> {

    List<InvoiceComponents> findByInvoice_Apartment_IdAndInvoice_DateBetween(Long apartmentId, LocalDate startDate, LocalDate endDate);

}
