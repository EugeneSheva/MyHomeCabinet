package com.example.myhome.home.repos;

import com.example.myhome.home.model.pages.AboutPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<AboutPage.Document, Long> {
}
