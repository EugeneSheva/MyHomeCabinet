package com.example.myhome.home.model.pages;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="page_service")
public class ServicesPage {

    @Id
    private int id = 1;

    //SEO
    private String seo_title, seo_description, seo_keywords;

}
