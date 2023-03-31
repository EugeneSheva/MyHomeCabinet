package com.example.myhome.home.model.pages;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="page_about")
public class AboutPage extends Page {

    //Информация
    private String title, description;
    private String director_photo;

    //Фотогалерея
    private String photos = "";

    //Дополнительная информация
    private String add_title, add_description;

    //Дополнительная фотогалерея
    private String add_photos = "";

    //Документы
    @OneToMany
    private List<Document> documents;

    @Data
    @Entity
    @Table(name="documents")
    public static class Document {
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;
        private String name;
        private String file;

        @ManyToOne
        private AboutPage page;
    }


}
