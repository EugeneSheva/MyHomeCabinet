package com.example.myhome.home.repos;

import com.example.myhome.home.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
}
