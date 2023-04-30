package com.example.myhome.home.service;

import com.example.myhome.home.model.IncomeExpenseItems;

import java.util.List;

public interface IncomeExpenseItemService {

    IncomeExpenseItems findById(Long id);
    List<IncomeExpenseItems> findAll();
    IncomeExpenseItems save(IncomeExpenseItems item);
    void deleteById(Long id);

}
