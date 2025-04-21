package com.wholesaleshop.demo_wholesale_shop.service;

import com.wholesaleshop.demo_wholesale_shop.dto.InvoiceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface InvoiceService {

    InvoiceDto saveInvoice(InvoiceDto invoiceDto);
    InvoiceDto updateInvoice(InvoiceDto invoiceDto);
    InvoiceDto deleteInvoice(Integer invoiceId);
    Page<InvoiceDto> getAllInvoices(Pageable pageable);
    List<InvoiceDto> searchInvoices(String query);
}
