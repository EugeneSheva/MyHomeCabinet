package com.example.myhome.home.model.pages;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="page_about")
public class AboutPage {

    @Id
    private int id = 1;

    //Информация
    private String title, description;
    private String director_photo;

    //Фотогалерея
    @ElementCollection
    private List<String> photos;

    //Дополнительная информация
    private String add_title, add_description;

    //Дополнительная фотогалерея
    @ElementCollection
    private List<String> add_photos;

    //Документы
    @ElementCollection
    private List<Document> documents;

    @Embeddable
    static class Document {
        private String name;
        private String photo;
    }

    //SEO
    private String seo_title, seo_description, seo_keywords;
}
