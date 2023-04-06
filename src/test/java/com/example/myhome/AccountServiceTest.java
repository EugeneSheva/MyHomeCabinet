package com.example.myhome;

import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.model.ApartmentAccount;
import com.example.myhome.home.model.CashBox;
import com.example.myhome.home.model.Invoice;
import com.example.myhome.home.repository.AccountRepository;
import com.example.myhome.home.service.AccountService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountServiceTest {

    @MockBean private AccountRepository accountRepository;
    @Autowired private AccountService accountService;

    @Test
    @Order(1)
    void sanityTest() {
        assertThat(accountRepository).isNotNull();
        assertThat(accountService).isNotNull();
    }

    @Test
    void calculatesBalancesAndDebtsCorrectly() {
        Random random = new Random();
        List<ApartmentAccount> accountList = new ArrayList<>();
        double total_positive_balance = 0.0;
        double total_debt = 0.0;
        for (int i = 0; i < 10; i++) {
            ApartmentAccount apartmentAccount = new ApartmentAccount();
            apartmentAccount.setTransactions(new ArrayList<>());
            apartmentAccount.setApartment(new Apartment());
            apartmentAccount.getApartment().setInvoiceList(new ArrayList<>());

            double account_balance = 0.0;

            for(int j = 0; j < 5; j++) {
                CashBox cashBox = new CashBox();
                cashBox.setCompleted(true);
                double transaction_amount = Math.floor(random.nextDouble()*10000);
                cashBox.setAmount(transaction_amount);
                account_balance += transaction_amount;
                apartmentAccount.getTransactions().add(cashBox);
            }

            for(int k = 0; k < 5; k++) {
                Invoice invoice = new Invoice();
                invoice.setCompleted(true);
                double invoice_price = Math.floor(random.nextDouble()*10000);
                invoice.setTotal_price(invoice_price);
                account_balance -= invoice_price;
                apartmentAccount.getApartment().getInvoiceList().add(invoice);
            }
            if(account_balance < 0) total_debt += account_balance;
            else total_positive_balance += account_balance;

            System.out.println("sum from account positive transactions: " + apartmentAccount.getTransactions().stream().map(CashBox::getAmount).reduce(Double::sum).orElse(0.0));
            System.out.println("sum from account negative invoices: " + apartmentAccount.getApartment().getInvoiceList().stream().map(Invoice::getTotal_price).reduce(Double::sum).orElse(0.0));
            System.out.println("account balance: " + account_balance);

            System.out.println("total debt: " + total_debt);
            System.out.println("total positive balance: " + total_positive_balance);
            System.out.println("======");
            accountList.add(apartmentAccount);
        }

        given(accountRepository.findAll()).willReturn(accountList);
        accountService.getSumOfAccountBalances();
        verify(accountRepository).findAll();
        assertThat(accountService.getSumOfAccountBalances()).isEqualTo(total_positive_balance);
        assertThat(accountService.getSumOfAccountDebts()).isEqualTo(total_debt);
    }

}
