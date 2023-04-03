package com.example.myhome.home.service;

import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.model.Building;
import com.example.myhome.home.model.BuildingDTO;
import com.example.myhome.home.model.IncomeExpenseItems;
import com.example.myhome.home.repos.BuildingRepository;
import com.example.myhome.home.repos.IncomeExpenseRepository;
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
public class IncomeExpenseItemService {
    private final IncomeExpenseRepository incomeExpenseRepository;

    public IncomeExpenseItems findById (Long id) { return incomeExpenseRepository.findById(id).orElseThrow(() -> new NotFoundException());}
    public List<IncomeExpenseItems> findAll() { return incomeExpenseRepository.findAll(); }
    public IncomeExpenseItems save(IncomeExpenseItems incomeExpenseItems) { return incomeExpenseRepository.save(incomeExpenseItems); }
    public void deleteById(Long id) { incomeExpenseRepository.deleteById(id); }




}
