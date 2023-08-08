package com.example.myhome.home.mapper;

import com.example.myhome.home.dto.*;
import com.example.myhome.home.model.*;
import com.example.myhome.home.repository.AccountRepository;
import com.example.myhome.home.repository.ApartmentRepository;
import com.example.myhome.home.repository.BuildingRepository;
import com.example.myhome.home.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvoiceDTOMapper {

    private  BuildingRepository buildingRepository;
    private  AccountRepository accountRepository;
    private  OwnerRepository ownerRepository;
    private  ApartmentRepository apartmentRepository;

    public Invoice fromDTOToInvoice(InvoiceDTO dto) {
        if(dto == null) return null;

        System.out.println(dto.getBuilding());
        System.out.println(dto.getApartment());
        System.out.println(dto.getAccount());
        System.out.println(dto.getOwner());

        Invoice invoice = new Invoice();
        invoice.setId(dto.getId());
        invoice.setDate(dto.getDate());
        invoice.setCompleted(dto.getCompleted());
        invoice.setStatus(dto.getStatus());
        invoice.setDateFrom(dto.getDateFrom());
        invoice.setDateTo(dto.getDateTo());
        invoice.setTotal_price(dto.getTotal_price());
        invoice.setComponents(dto.getComponents());
        invoice.setSection(dto.getSection());
        invoice.setTariff(dto.getTariff());

        invoice.getComponents().forEach(comp -> comp.setInvoice(invoice));

        invoice.setBuilding(buildingRepository.getReferenceById(dto.getBuilding().getId()));
        invoice.setAccount(accountRepository.getReferenceById(dto.getAccount().getId()));
        invoice.setOwner(ownerRepository.getReferenceById(dto.getOwner().getId()));
        invoice.setApartment(apartmentRepository.getReferenceById(dto.getApartment().getId()));

        return invoice;
    }

    public InvoiceDTO fromInvoiceToDTO(Invoice invoice) {
        System.out.println(invoice);
        if(invoice == null) return null;

        InvoiceDTO dto = InvoiceDTO.builder()
                                    .id(invoice.getId())
                                    .date(invoice.getDate())
                                    .completed(invoice.getCompleted())
                                    .section(invoice.getSection())
                                    .status(invoice.getStatus())
                                    .dateFrom(invoice.getDateFrom())
                                    .dateTo(invoice.getDateTo())
                                    .total_price(invoice.getTotal_price())
                                    .components(invoice.getComponents())
                                    .tariff(invoice.getTariff())
                                    .build();

        ApartmentAccount account = invoice.getApartment().getAccount();
        if(account != null) dto.setAccount(ApartmentAccountDTO.builder()
                            .id(account.getId())
                            .build());

        Apartment apartment = invoice.getApartment();
        System.out.println( "apartment  " + apartment);
        if(apartment != null) dto.setApartment(ApartmentDTO.builder()
                .id(apartment.getId())
                .fullName("кв." + apartment.getNumber() + ", " + apartment.getBuilding().getName())
                .build());

        Owner owner = invoice.getApartment().getOwner();
        if(owner != null) dto.setOwner(OwnerDTO.builder()
                .id(owner.getId())
                .fullName(owner.getFullName())
                .phone_number(owner.getPhone_number())
                .build());

        Building building = invoice.getApartment().getBuilding();
        if(building != null) dto.setBuilding(BuildingDTO.builder()
                .id(building.getId())
                .name(building.getName())
                .build());

        return dto;
    }

}
