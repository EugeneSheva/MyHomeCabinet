package com.example.myhome.home.service;
import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.model.IncomeExpenseItems;
import com.example.myhome.home.repository.IncomeExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class IncomeExpenseItemService {
    private final IncomeExpenseRepository incomeExpenseRepository;

    public IncomeExpenseItems findById (Long id) { return incomeExpenseRepository.findById(id).orElseThrow(() -> new NotFoundException());}
    public List<IncomeExpenseItems> findAll() { return incomeExpenseRepository.findAll(); }
    public IncomeExpenseItems save(IncomeExpenseItems incomeExpenseItems) { return incomeExpenseRepository.save(incomeExpenseItems); }
    public void deleteById(Long id) { incomeExpenseRepository.deleteById(id); }




}
