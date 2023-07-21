package com.example.myhome.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    EmailService emailService;

    @MockBean
    JavaMailSender mailSender;

    @Test
    void contextLoads() {

    }

    @Test
    void sendTest() {
        when(mailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        emailService.send("test", "test");
    }

    @Test
    void failSendTest() {
        when(mailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        doAnswer(invocation -> {throw new MessagingException();}).when(mailSender).send((MimeMessage) any());
        emailService.send("test", "test");
    }
}
