package com.example.myhome.home.repository;

import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.model.Message;
import com.example.myhome.home.model.Owner;
import com.example.myhome.home.specification.ApartmentSpecification;
import com.example.myhome.home.specification.MessageSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long>, JpaSpecificationExecutor<Message> {

//    @Query("SELECT m FROM Message m JOIN m.receivers r WHERE r.id = :ownerId")
//    List<Message> findAllMessagesByOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT m FROM Message m JOIN m.receivers r JOIN r.messages rm WHERE rm.id = :ownerId GROUP BY m.id")
    List<Message> findAllMessagesByOwnerId(@Param("ownerId") Long ownerId);

    Page<Message> findAll(Pageable pageable);
    default Page<Message> findByFilters(String text, Pageable pageable) {
        Specification<Message> spec = Specification.where(null);

        if (text != null && !text.isEmpty()) {
            spec = spec.and(MessageSpecification.textContains(text));
        }
        return findAll(spec,pageable);
    }

}
