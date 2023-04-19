package com.example.myhome.home.repository;

import com.example.myhome.home.model.Message;
import com.example.myhome.home.model.Owner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Long countAllBy();
    Optional<Owner> findByEmail(String email);
    Boolean existsByEmail(String email);

//    List<Owner> findByFirstNameContainingIgnoreCase(String firstName, Pageable pageable);

    @Query(value="SELECT o FROM Owner o WHERE o.first_name LIKE %:name% OR o.last_name LIKE %:name% OR o.fathers_name LIKE %:name%")
    Page<Owner> findByName(String name, Pageable pageable);
}
