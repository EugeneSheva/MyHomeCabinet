package com.example.myhome;

import com.example.myhome.home.repository.*;
import com.example.myhome.home.service.impl.InvoiceServiceImpl;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@SpringBootApplication(scanBasePackages = {"com.example.myhome.home", "com.example.myhome.util"})
@EnableJpaRepositories(basePackages = {"com.example.myhome.home.repository", "com.example.myhome.home.service.registration"})
@EnableEncryptableProperties
@Log4j2
public class MyHomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyHomeApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void b() throws IOException {
        log.info("NEW LOG MSG");
        log.error("NEW ERROR MSG");
        log.warn("NEW WARN MSG");
    }

}
