package com.example.myhome.home.repos;

import com.example.myhome.home.model.InvoiceComponents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceComponentRepository extends JpaRepository<InvoiceComponents, Long> {
}
