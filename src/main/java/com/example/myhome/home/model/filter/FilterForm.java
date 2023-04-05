package com.example.myhome.home.model.filter;

import lombok.Data;

@Data
public class FilterForm {

    private Long building_filter;
    private Long service_filter;
    private Long apartment_filter;
    private String section_filter;

}
