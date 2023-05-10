package com.example.myhome.home.service;

import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.model.*;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.MessageRepository;
import com.example.myhome.util.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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


    public Page<Message> findAllBySpecification(FilterForm form, Integer page, Integer size, Long ownerId) {
        System.out.println("service");
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Message>messageList = messageRepository.findByFilters(form.getDescription(), ownerId, pageable);
        System.out.println("messageList" +messageList);
        return messageList;
    }

    public Page<Message> findAll(Pageable pageable) {
        return messageRepository.findAll(pageable);
    }
}
