package com.example.myhome.home.repository;


import com.example.myhome.home.model.ForgotPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgotPasswordTokenRepository extends JpaRepository<ForgotPasswordToken, Long> {
    boolean existsByToken(String token);
    Optional<ForgotPasswordToken> findByToken(String token);
    void deleteByToken(String token);
}
