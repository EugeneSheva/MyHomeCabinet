package com.example.myhome.home.repos;

import com.example.myhome.home.model.BuildingSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingSectionRepository extends JpaRepository<BuildingSection, Long> {
}
