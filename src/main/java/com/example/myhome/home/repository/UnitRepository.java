package com.example.myhome.home.repository;

import com.example.myhome.home.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
    Optional<Unit> findByName(String name);
    Optional<String> findNameById(long unit_id);
    boolean existsByName(String name);
}
