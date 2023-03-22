package com.example.myhome.home.model.pages;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="page_service")
public class ServicesPage extends Page {

}
