package com.example.myhome.home.repository;

import com.example.myhome.home.model.IncomeExpenseType;
import com.example.myhome.home.model.Invoice;
import com.example.myhome.home.model.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long>, JpaSpecificationExecutor<Invoice> {

    @Query("SELECT MAX(i.id) FROM Invoice i")
    Optional<Long> getMaxId();

    @Query("SELECT SUM(i.total_price) FROM Invoice i WHERE MONTH(i.date) = :month AND YEAR(i.date) = :year AND i.completed=true")
    Double getSumIvoice(@Param("month") int month, @Param("year") int year);

    @Query("SELECT SUM(i.total_price) FROM Invoice i WHERE MONTH(i.date) = :month AND YEAR(i.date) = :year AND i.completed=true AND i.status= :invoiceStatus")
    Double getSumPaidIvoice(@Param("month") int month, @Param("year") int year, @Param("invoiceStatus") InvoiceStatus invoiceStatus);

}
