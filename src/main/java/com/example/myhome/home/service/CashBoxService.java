package com.example.myhome.home.service;

import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.model.CashBox;
import com.example.myhome.home.repository.CashBoxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CashBoxService {
    private final CashBoxRepository cashBoxRepository;


    public CashBox findById (Long id) { return cashBoxRepository.findById(id).orElseThrow(() -> new NotFoundException());}

    public List<CashBox> findAll() { return cashBoxRepository.findAll(); }

    public CashBox save(CashBox cashBox) { return cashBoxRepository.save(cashBox); }

    public void deleteById(Long id) { cashBoxRepository.deleteById(id); }




}
