package com.example.myhome.home.service;

import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.model.CashBox;
import com.example.myhome.home.model.Owner;
import com.example.myhome.home.model.OwnerDTO;
import com.example.myhome.home.repos.CashBoxRepository;
import com.example.myhome.home.repos.OwnerRepository;
import com.example.myhome.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CashBoxService {
    private final CashBoxRepository cashBoxRepository;


    public CashBox findById (Long id) { return cashBoxRepository.findById(id).orElseThrow(() -> new NotFoundException());}

    public List<CashBox> findAll() { return cashBoxRepository.findAll(); }

    public CashBox save(CashBox cashBox) { return cashBoxRepository.save(cashBox); }

    public void deleteById(Long id) { cashBoxRepository.deleteById(id); }




}
