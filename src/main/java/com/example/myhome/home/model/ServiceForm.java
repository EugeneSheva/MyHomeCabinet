package com.example.myhome.home.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class ServiceForm {
    private List<Service> serviceList;
    private List<Unit> unitList;
}
