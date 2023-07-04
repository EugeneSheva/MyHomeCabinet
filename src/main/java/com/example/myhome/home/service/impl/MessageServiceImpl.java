package com.example.myhome.home.service.impl;

import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.model.*;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.MessageRepository;
import com.example.myhome.home.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    @Value("${upload.path}")
    private String uploadPath;
    private final MessageRepository messageRepository;


    @Override
    public Message findById(Long id) { return messageRepository.findById(id).orElseThrow(() -> new NotFoundException());}

    @Override
    public List<Message> findAll() { return messageRepository.findAll(); }

    @Override
    public Message save(Message message) { return messageRepository.save(message); }

    @Override
    public void deleteById(Long id) { messageRepository.deleteById(id); }


    @Override
    public Page<Message> findAllBySpecification(FilterForm form, Integer page, Integer size, Long ownerId) {
        System.out.println("service");

        Pageable pageable = PageRequest.of(page-1, size).withSort(Sort.by(Sort.Direction.DESC, "date"));
        Page<Message>messageList = messageRepository.findByFilters(form.getDescription(), ownerId, pageable);
        System.out.println("messageList" +messageList);
        return messageList;
    }

    @Override
    public Page<Message> findAll(Pageable pageable) {
        return messageRepository.findAll(pageable);
    }
}
