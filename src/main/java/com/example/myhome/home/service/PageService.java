package com.example.myhome.home.service;

import com.example.myhome.home.repository.PageRoleDisplayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PageService {

    @Autowired
    private PageRoleDisplayRepository repository;
}
