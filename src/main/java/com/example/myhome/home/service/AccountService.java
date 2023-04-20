package com.example.myhome.home.service;

import com.example.myhome.home.model.Apartment;
import com.example.myhome.home.model.ApartmentAccount;
import com.example.myhome.home.model.Building;
import com.example.myhome.home.model.Owner;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.AccountRepository;
import com.example.myhome.home.repository.ApartmentRepository;
import com.example.myhome.home.repository.specifications.AccountSpecifications;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired private ApartmentService apartmentService;
    @Autowired private BuildingService buildingService;
    @Autowired private OwnerService ownerService;

    public List<ApartmentAccount> findAll() {return accountRepository.findAll();}

    public Page<ApartmentAccount> findAllBySpecification(FilterForm filters) {

        if(filters.getPage() == null) return accountRepository.findAll(PageRequest.of(0, 4));

        log.info("Filters found!");
        log.info(filters.toString());

        Long id = filters.getId();
        Boolean active = filters.getActive();
//        Apartment apartment = (filters.getApartment() != null) ? apartmentService.findByNumber(filters.getApartment()) : null;
        Long apartment = (filters.getApartment() != null) ? filters.getApartment() : null;
        Building building = (filters.getBuilding() != null) ? buildingService.findById(filters.getBuilding()) : null;
        String section = filters.getSection();
        Owner owner = (filters.getOwner() != null) ? ownerService.findById(filters.getOwner()) : null;
        Boolean debt = filters.getDebt();

        Specification<ApartmentAccount> specification =
                Specification.where(AccountSpecifications.hasId(id)
                        .and(AccountSpecifications.isActive(active))
                        .and(AccountSpecifications.hasApartmentNumber(apartment))
                        .and(AccountSpecifications.hasBuilding(building))
                        .and(AccountSpecifications.hasSection(section))
                        .and(AccountSpecifications.hasOwner(owner))
                        .and(AccountSpecifications.hasDebt(debt)));

        return accountRepository.findAll(specification, PageRequest.of(filters.getPage()-1, 4));
    }

    public ApartmentAccount getAccountById(long account_id) {
        return accountRepository.findById(account_id).orElseThrow();
    }
    public ApartmentAccount getAccountWithBiggestId() {return accountRepository.findFirstByOrderByIdDesc().orElseThrow();}
    public ApartmentAccount getAccountNumberFromFlat(long flat_id) {return accountRepository.findByApartmentId(flat_id).orElseThrow();}

    public ApartmentAccount save(ApartmentAccount account) {return accountRepository.save(account);}

    @Transactional
    public void deleteAccountById(long account_id) {
        ApartmentAccount account = getAccountById(account_id);
        account.getTransactions().clear();
        account.getInvoices().forEach(inv -> inv.setAccount(null));
        account.getInvoices().clear();
        accountRepository.delete(account);
    }

    public boolean apartmentHasAccount(long apartment_id) {
        return (apartmentRepository.findById(apartment_id).orElseThrow().getAccount() != null);
    }
    public Long getQuantity() { return accountRepository.countAllBy();}

    public Long getMaxId() {return accountRepository.getMaxId().orElse(null);}

    public Double getSumOfAccountBalances() {
        return accountRepository.findAll().stream()
                .map(ApartmentAccount::getBalance)
                .filter(balance -> balance > 0)
                .reduce(Double::sum).orElse(0.0);
    }
    public Double getSumOfAccountDebts() {
        return accountRepository.findAll().stream()
                .map(ApartmentAccount::getBalance)
                .filter(balance -> balance < 0)
                .reduce(Double::sum).orElse(0.0);
    }


}
