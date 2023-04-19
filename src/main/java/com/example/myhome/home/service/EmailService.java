package com.example.myhome.home.service;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.Objects;

@Service
@Log
@PropertySource("classpath:mail.properties")
public class EmailService {

    @Autowired
    Environment env;

    @Value("${spring.mail.username}")
    private String EMAIL_SENDER;

    @Autowired
    private JavaMailSender mailSender;

    public void send(String to, String email) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Email confirmation");
            helper.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));

            log.info(EMAIL_SENDER);
            log.info(env.getProperty("spring.mail.username"));

            mailSender.send(message);

        } catch (MessagingException e) {
            log.info("fail to send email, msg: ");
            log.info(Arrays.toString(e.getStackTrace()));
        }
    }
}
