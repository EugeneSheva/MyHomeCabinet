package com.example.myhome.home.service;

import com.example.myhome.home.model.Message;
import com.example.myhome.home.model.filter.FilterForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {
    Message findById(Long id);

    Message save(Message message);

    Page<Message> findAllBySpecification(FilterForm form, Integer page, Integer size, Long ownerId);

}
