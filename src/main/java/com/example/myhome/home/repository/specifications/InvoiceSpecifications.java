package com.example.myhome.home.repository.specifications;

import com.example.myhome.home.model.Invoice;
import com.example.myhome.home.model.InvoiceStatus;
import com.example.myhome.home.model.Invoice_;
import org.springframework.data.jpa.domain.Specification;

public class InvoiceSpecifications {

    public static Specification<Invoice> invoiceHasIdLike(Long id){
        return (root, query, cb) -> cb.like(root.get(Invoice_.id.toString()), "%" + id + "%");
    }

    public static Specification<Invoice> invoiceHasStatus(InvoiceStatus status) {
        return (root, query, cb) -> cb.equal(root.get(Invoice_.status), status);
    }

}
