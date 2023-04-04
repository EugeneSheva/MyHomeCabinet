package com.example.myhome.home.service;

import com.example.myhome.home.model.ApartmentAccount;
import com.example.myhome.home.repository.AccountRepository;
import com.example.myhome.home.repository.ApartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ApartmentRepository apartmentRepository;

    public List<ApartmentAccount> findAll() {return accountRepository.findAll();}

    public ApartmentAccount getAccountById(long account_id) {
        return accountRepository.findById(account_id).orElseThrow();
    }
    public ApartmentAccount getAccountWithBiggestId() {return accountRepository.findFirstByOrderByIdDesc().orElseThrow();}
    public ApartmentAccount getAccountNumberFromFlat(long flat_id) {return accountRepository.findByApartmentId(flat_id).orElseThrow();}

    public ApartmentAccount save(ApartmentAccount account) {return accountRepository.save(account);}

    public void deleteAccountById(long account_id) {accountRepository.deleteById(account_id);}

    public boolean apartmentHasAccount(long apartment_id) {
        return (apartmentRepository.findById(apartment_id).orElseThrow().getAccount() != null);
    }
    public Long getQuantity() { return accountRepository.countAllBy();}


}
