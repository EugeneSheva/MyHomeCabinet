package com.example.myhome.home.model.pages;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(name="page_contacts")
public class ContactsPage extends Page {

    //Контактная информация
    @NotBlank
    private String title, description, website_link;

    //Контакты
    @NotBlank
    private String name, location, address, phone, email;

    //Карта
    @NotBlank
    private String map_code;

}
