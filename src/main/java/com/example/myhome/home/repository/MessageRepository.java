package com.example.myhome.home.repository;

import com.example.myhome.home.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
