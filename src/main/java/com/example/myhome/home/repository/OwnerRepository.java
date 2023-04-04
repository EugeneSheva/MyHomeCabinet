package com.example.myhome.home.repository;

import com.example.myhome.home.model.Message;
import com.example.myhome.home.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Long countAllBy();
}
