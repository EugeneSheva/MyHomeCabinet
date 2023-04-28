package com.example.myhome.home.repository;

import com.example.myhome.home.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

//    @Query("SELECT m FROM Message m JOIN m.receivers r WHERE r.id = :ownerId")
//    List<Message> findAllMessagesByOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT m FROM Message m JOIN m.receivers r JOIN r.messages rm WHERE rm.id = :ownerId GROUP BY m.id")
    List<Message> findAllMessagesByOwnerId(@Param("ownerId") Long ownerId);

}
