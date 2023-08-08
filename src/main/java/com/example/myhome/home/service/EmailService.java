package com.example.myhome.home.service;

public interface EmailService {

    void send(String recipientAddress, String emailContent);
    void sendWithAttachment(String recipientAddress, String fileName);
}
