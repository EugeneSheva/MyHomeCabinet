package com.example.myhome.home.controller;

import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.model.BuildingDTO;
import com.example.myhome.home.model.Message;
import com.example.myhome.home.model.Owner;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.OwnerRepository;
import com.example.myhome.home.service.BuildingService;
import com.example.myhome.home.service.OwnerService;
import com.example.myhome.home.validator.OwnerValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/ownersrest")
@Log
public class OwnerRestController {

    @Value("${upload.path}")
    private String uploadPath;
    private final OwnerService ownerService;
    private final OwnerValidator ownerValidator;
    private final OwnerRepository ownerRepository;
    private final BuildingService buildingService;

    @GetMapping("/owners")
    public Page<Owner> getOwners(@RequestParam(name = "page", defaultValue = "0") int page,
                                 @RequestParam(name = "size", defaultValue = "2") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        System.out.println("page" + page + "size" + size);

        return ownerRepository.findAll(pageable);
    }
}
