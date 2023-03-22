package com.example.myhome.home.model.pages;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="page_contacts")
public class ContactsPage {

    @Id
    private int id = 1;

    //Контактная информация
    private String title, description, website_link;

    //Контакты
    private String name, location, address, phone, email;

    //Карта
    private String map_code;

    //SEO
    private String seo_title, seo_description, seo_keywords;
}
