package com.example.myhome;

import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.model.IncomeExpenseItems;
import com.example.myhome.home.repository.IncomeExpenseRepository;
import com.example.myhome.home.service.impl.IncomeExpenseItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class IncomeExpenseServiceTest {

    @MockBean private IncomeExpenseRepository incomeExpenseRepository;
    @Autowired private IncomeExpenseItemServiceImpl service;
    @Autowired private LocalValidatorFactoryBean validator;

    IncomeExpenseItems item;

    @BeforeEach
    void createItem() {
        item = new IncomeExpenseItems();
        item.setId(1L);
        item.setName("Test");
    }

    @Test
    void sanityCheck() {
        assertThat(incomeExpenseRepository).isNotNull();
    }

    @Test
    void canFindItemById() {

        given(incomeExpenseRepository.findById(1L)).willReturn(Optional.of(item));
        service.findById(1L);
        verify(incomeExpenseRepository).findById(1L);
        assertThat(service.findById(1L)).isEqualTo(item);
    }

    @Test
    void throwsExceptionIfItemNotFound() {
        assertThrows(NotFoundException.class, () -> service.findById(1L));
    }

    @Test
    void validatesItemName() {
        item.setName(null);

        Set<ConstraintViolation<IncomeExpenseItems>> violations = validator.validate(item);
        assertThat(violations.size()).isEqualTo(1);
        System.out.println(violations);
    }
}
