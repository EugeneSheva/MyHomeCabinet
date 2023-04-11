package com.example.myhome.home.repository;

import com.example.myhome.home.model.PageRoleDisplay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRoleDisplayRepository extends JpaRepository<PageRoleDisplay, Long> {
}
