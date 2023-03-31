package com.example.myhome.home.service;

import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.model.Message;
import com.example.myhome.home.model.Owner;
import com.example.myhome.home.repository.MessageRepository;
import com.example.myhome.home.repository.OwnerRepository;
import com.example.myhome.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {
    @Value("${upload.path}")
    private String uploadPath;
    private final MessageRepository messageRepository;


    public Message findById (Long id) { return messageRepository.findById(id).orElseThrow(() -> new NotFoundException());}

    public List<Message> findAll() { return messageRepository.findAll(); }

    public Message save(Message message) { return messageRepository.save(message); }

    public void deleteById(Long id) { messageRepository.deleteById(id); }


}
