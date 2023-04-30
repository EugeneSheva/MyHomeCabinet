package com.example.myhome.home.service;

import com.example.myhome.home.dto.ApartmentAccountDTO;
import com.example.myhome.home.model.ApartmentAccount;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.util.MappingUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public interface AccountService {

    List<ApartmentAccount> findAllAccounts();
    Page<ApartmentAccount> findAllAccountsByPage(Pageable pageable);
    Page<ApartmentAccount> findAllAccountsByFiltersAndPage(FilterForm filters, Pageable pageable);

    Long getMaxAccountId();
    Double getSumOfAccountBalances();
    Double getSumOfAccountDebts();

    ApartmentAccount findAccountById(Long account_id);
    ApartmentAccount getAccountNumberFromFlat(Long flat_id);

    ApartmentAccount saveAccount(ApartmentAccount account);

    @Transactional
    void deleteAccountById(Long account_id);

    Specification<ApartmentAccount> buildSpecFromFilters(FilterForm filters);

    default ApartmentAccount fromDTO(ApartmentAccountDTO dto) {return MappingUtils.fromDTOToAccount(dto);}
    default ApartmentAccountDTO toDTO(ApartmentAccount account) {
        return MappingUtils.fromAccountToDTO(account);
    }
    default List<ApartmentAccountDTO> toDTO(List<ApartmentAccount> accountList) {
        return accountList.stream().map(MappingUtils::fromAccountToDTO).collect(Collectors.toList());
    }
    default Page<ApartmentAccountDTO> toDTO(Page<ApartmentAccount> accountPage) {
        return new PageImpl<>(
                accountPage.getContent().stream().map(MappingUtils::fromAccountToDTO).collect(Collectors.toList()),
                accountPage.getPageable(),
                accountPage.getTotalElements()
        );
    }

    default Page<ApartmentAccountDTO> toPage(List<ApartmentAccountDTO> list, Pageable pageable, Long totalElements) {
        return new PageImpl<>(list, pageable, totalElements);
    }

}
