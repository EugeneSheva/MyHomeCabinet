package com.example.myhome.home.service;

import com.example.myhome.home.dto.CashBoxDTO;
import com.example.myhome.home.model.CashBox;
import com.example.myhome.home.model.IncomeExpenseType;
import com.example.myhome.home.model.filter.FilterForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CashBoxService {
    CashBox findById(Long id);

    List<CashBox> findAll();

    Page<CashBox> findAll(Pageable pageable);

    CashBox save(CashBox cashBox);

    Page<CashBoxDTO> findAllBySpecification2(FilterForm filters, Integer page, Integer size);

    void deleteById(Long id);

    Long getMaxId();

    Double calculateBalance();

    List<Double> getListSumIncomeByMonth();

    List<Double> getListSumExpenceByMonth();

    List<String> getListOfMonthName();

    IncomeExpenseType getIncomeExpenseTypeFromString(String incomeExpenseTypeString);

    Boolean getIsCompleteFromString(String isCopmlete);
}
