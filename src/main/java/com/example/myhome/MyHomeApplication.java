package com.example.myhome;

import com.example.myhome.home.model.*;
import com.example.myhome.home.repository.*;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication(scanBasePackages = {"com.example.myhome.home", "com.example.myhome.util"})
@EnableJpaRepositories(basePackages = {"com.example.myhome.home.repository", "com.example.myhome.home.service.registration"})
@EnableEncryptableProperties
@Log
public class MyHomeApplication {

    @Autowired private PageRoleDisplayRepository repository;
    @Autowired private OwnerRepository ownerRepository;
    @Autowired private AdminRepository adminRepository;

    public static void main(String[] args) {
        SpringApplication.run(MyHomeApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void populateDB(){

    }

}
