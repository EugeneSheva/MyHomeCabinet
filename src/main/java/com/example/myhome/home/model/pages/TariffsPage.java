package com.example.myhome.home.model.pages;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="page_tariffs")
public class TariffsPage extends Page {

    //Информация
    private String title, description;

    //Изображения
    @ElementCollection
    private List<TariffBlock> blocks;

    @Embeddable
    static class TariffBlock {
        private String title;
        private String photo;
    }

}
