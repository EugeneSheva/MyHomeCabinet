package com.example.myhome.home.mapper;

import com.example.myhome.home.dto.InvoiceDTO;
import com.example.myhome.home.model.Invoice;
import org.springframework.stereotype.Component;

@Component
public class InvoiceDTOMapper {

    public Invoice fromDTOToInvoice(InvoiceDTO dto) {
        if(dto == null) return null;

        Invoice invoice = new Invoice();
        invoice.setId(dto.getId());
        invoice.setDate(dto.getDate());
        invoice.setCompleted(dto.getCompleted());
        invoice.setStatus(dto.getStatus());
        invoice.setDateFrom(dto.getDateFrom());
        invoice.setDateTo(dto.getDateTo());
        invoice.setTotal_price(dto.getTotal_price());
        invoice.setComponents(dto.getComponents());

        return invoice;
    }

    public InvoiceDTO fromInvoiceToDTO(Invoice invoice) {
        if(invoice == null) return null;

        return InvoiceDTO.builder()
                .id(invoice.getId())
                .date(invoice.getDate())
                .completed(invoice.getCompleted())
                .status(invoice.getStatus())
                .dateFrom(invoice.getDateFrom())
                .dateTo(invoice.getDateTo())
                .total_price(invoice.getTotal_price())
                .components(invoice.getComponents())
                .build();
    }

}
