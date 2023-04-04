package com.example.myhome;

import com.example.myhome.home.model.Tariff;
import com.example.myhome.home.repository.TariffRepository;
import com.example.myhome.home.service.TariffService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TariffServiceTests {

    @MockBean
    private TariffRepository tariffRepository;

    @Autowired
    private TariffService tariffService;

    @Test
    @Order(1)
    void check() {
        assertThat(tariffRepository).isNotNull();
        assertThat(tariffService).isNotNull();
    }

    @Test
    void canSaveTariff() {
        Tariff tariff = new Tariff();
        tariff.setName("TEST");

        Tariff expected = new Tariff();
        expected.setId(1L);
        expected.setName("TEST");

        given(tariffRepository.save(tariff)).willReturn(expected);

        tariffService.saveTariff(tariff);
        verify(tariffRepository).save(tariff);

        assertThat(tariffService.saveTariff(tariff)).isEqualTo(expected);
    }

//    @Test
//    void cantSaveTariffWithoutName() {
//        Tariff tariff = new Tariff();
//
//        Tariff expected = new Tariff();
//        expected.setId(1L);
//
//        tariffService.saveTariff(tariff);
//    }

    @Test
    void canLoadTariffs() {
        List<Tariff> tariffList = List.of(new Tariff(), new Tariff());

        given(tariffRepository.findAll()).willReturn(tariffList);

        tariffService.findAllTariffs();
        verify(tariffRepository).findAll();
        assertThat(tariffService.findAllTariffs())
                .isNotNull()
                .hasSize(2);
    }

    @Test
    void canLoadSingleTariffById() {
        Tariff tariff = new Tariff();
        tariff.setId(2L);
        tariff.setName("TEST");

        given(tariffRepository.findById(2L)).willReturn(Optional.of(tariff));
        tariffService.findTariffById(2L);
        verify(tariffRepository).findById(2L);
        assertThat(tariffService.findTariffById(2L)).isEqualTo(tariff);
    }

    @Test
    void canSaveAndThenLoadTariff() {
        Tariff tariff = new Tariff();
        tariff.setName("TEST");

        Tariff expected = new Tariff();
        expected.setId(1L);
        expected.setName("TEST");

        given(tariffRepository.save(tariff)).willReturn(expected);
        given(tariffRepository.findById(1L)).willReturn(Optional.of(expected));

        tariffService.saveTariff(tariff);
        verify(tariffRepository).save(tariff);

        assertThat(tariffService.findTariffById(1L)).isEqualTo(new Tariff(1L, "TEST"));
    }

    // todo: сделать тесты добавления компонентов в тарифы
}
