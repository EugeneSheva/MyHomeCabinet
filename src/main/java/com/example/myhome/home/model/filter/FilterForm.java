package com.example.myhome.home.model.filter;

import lombok.Data;

import java.lang.reflect.Field;

@Data
public class FilterForm {

    private Long building;
    private Long service;
    private Long apartment;
    private String section;

    private Long id;
    private String datetime;
    private String description;

    private String master_type;
    private Long master;

    private Long owner;
    private String phone;

    private String status;

    private Boolean active;
    private Boolean debt;
    private Boolean completed;

    public FilterForm() {
    }

    public boolean filtersPresent() throws IllegalAccessException {
        for (Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if(field.get(this) != null) {
                field.setAccessible(false);
                return true;
            }
            field.setAccessible(false);
        }
        return false;
    }

}
