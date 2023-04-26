package com.example.myhome.home.controller;

import com.example.myhome.home.model.Owner;
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
import org.springframework.web.bind.annotation.*;


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
