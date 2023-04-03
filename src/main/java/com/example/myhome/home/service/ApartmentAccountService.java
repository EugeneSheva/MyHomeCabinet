package com.example.myhome.home.service;

import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.model.ApartmentAccount;
import com.example.myhome.home.model.ApartmentAccountDTO;
import com.example.myhome.home.model.ApartmentDTO;
import com.example.myhome.home.repos.AccountRepository;
import com.example.myhome.home.repos.ApartmentRepository;
import com.example.myhome.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApartmentAccountService {

    private final AccountRepository accountRepository;


    public ApartmentAccount findById (Long id) { return accountRepository.findById(id).orElseThrow(() -> new NotFoundException());}

    public ApartmentAccount save(ApartmentAccount apartmentAccount) { return accountRepository.save(apartmentAccount); }

    public void deleteById(Long id) { accountRepository.deleteById(id); }

    public List<ApartmentAccount> findAll() { return accountRepository.findAll(); }

    public List<ApartmentAccountDTO> findDtoApartmentAccounts() {
        List<ApartmentAccountDTO>apartmentAccountDTOS= new ArrayList<>();
        for (ApartmentAccount apartmentAccount : accountRepository.findAll()) {
            apartmentAccountDTOS.add(new ApartmentAccountDTO(apartmentAccount.getId(), apartmentAccount.getIsActive(),apartmentAccount.getNumber(),apartmentAccount.getApartment().getId(),apartmentAccount.getBalance()));
        }
        return apartmentAccountDTOS; }


}
