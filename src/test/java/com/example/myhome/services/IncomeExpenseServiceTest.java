//package com.example.myhome.services;
//
//import com.example.myhome.home.exception.NotFoundException;
//import com.example.myhome.home.model.IncomeExpenseItems;
//import com.example.myhome.home.repository.IncomeExpenseRepository;
//import com.example.myhome.home.service.IncomeExpenseItemService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
//
//import javax.validation.ConstraintViolation;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.willThrow;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//public class IncomeExpenseServiceTest {
//
//    @MockBean private IncomeExpenseRepository incomeExpenseRepository;
//    @Autowired private IncomeExpenseItemService service;
//    @Autowired private LocalValidatorFactoryBean validator;
//
//    IncomeExpenseItems item;
//
//    @BeforeEach
//    void createItem() {
//        item = new IncomeExpenseItems();
//        item.setId(null);
//        item.setName("Test");
//    }
//
//    @Test
//    void sanityCheck() {
//        assertThat(incomeExpenseRepository).isNotNull();
//        assertThat(service).isNotNull();
//    }
//
//    @Test
//    void canFindAllTest() {
//        when(incomeExpenseRepository.findAll()).thenReturn(List.of(new IncomeExpenseItems()));
//        assertThat(service.findAll().size()).isEqualTo(1);
//    }
//
//    @Test
//    void canFindItemById() {
//        given(incomeExpenseRepository.findById(1L)).willReturn(Optional.of(item));
//        service.findById(1L);
//        verify(incomeExpenseRepository).findById(1L);
//        assertThat(service.findById(1L)).isEqualTo(item);
//    }
//
//    @Test
//    void throwsExceptionIfItemNotFound() {
//        when(incomeExpenseRepository.findById(any())).thenThrow(new NotFoundException());
////        assertThrows(NotFoundException.class, () -> service.findById(10000L));
//        assertThat(service.findById(10000L)).isNull();
//    }
//
//    @Test
//    void canSaveItem() {
//        IncomeExpenseItems expectedItem = new IncomeExpenseItems();
//        expectedItem.setId(1L);
//        expectedItem.setName("test");
//        given(incomeExpenseRepository.save(any(IncomeExpenseItems.class))).willReturn(expectedItem);
//        IncomeExpenseItems savedItem = service.save(item);
//        verify(incomeExpenseRepository).save(item);
//        assertThat(savedItem).isEqualTo(expectedItem);
//        assertThat(savedItem.getId()).isEqualTo(expectedItem.getId());
//    }
//
//    @Test
//    void canThrowErrorOnSaveItemTest() {
//        when(incomeExpenseRepository.save(any(IncomeExpenseItems.class))).thenThrow(new NotFoundException());
////        assertThrows(NotFoundException.class, () -> service.save(new IncomeExpenseItems()));
//        assertThat(service.save(new IncomeExpenseItems())).isNull();
//    }
//
//    @Test
//    void canDeleteItemTest() {
//        service.deleteById(1L);
//    }
//
//    @Test
//    void throwsErrorOnDeleteItemTest() {
//        willThrow(new NotFoundException()).given(incomeExpenseRepository).deleteById(anyLong());
////        assertThrows(NotFoundException.class, () -> service.deleteById(1L));
//        service.deleteById(1L);
//    }
//
//    @Test
//    void validatesItemName() {
//        item.setName(null);
//        Set<ConstraintViolation<IncomeExpenseItems>> violations = validator.validate(item);
//        assertThat(violations.size()).isEqualTo(1);
//    }
//}
