package com.wholesaleshop.demo_wholesale_shop.service.impl;

import com.wholesaleshop.demo_wholesale_shop.dto.InvoiceDto;
import com.wholesaleshop.demo_wholesale_shop.entity.Invoice;
import com.wholesaleshop.demo_wholesale_shop.entity.Orders;
import com.wholesaleshop.demo_wholesale_shop.repo.InvoiceRepo;
import com.wholesaleshop.demo_wholesale_shop.repo.OrderRepo;
import com.wholesaleshop.demo_wholesale_shop.repo.PaymentRepo;
import com.wholesaleshop.demo_wholesale_shop.service.InvoiceService;
import com.wholesaleshop.demo_wholesale_shop.utils.InvoiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    InvoiceRepo invoiceRepo;
    @Autowired
    InvoiceMapper invoiceMapper;
    @Autowired
    OrderRepo orderRepo;

    @Override
    public InvoiceDto saveInvoice(InvoiceDto invoiceDto) {
        Optional<Orders> orderOpt = orderRepo.findById(invoiceDto.getOrderId());

        if (orderOpt.isPresent()) {
            Orders order = orderOpt.get();

            Invoice invoice = new Invoice();
            invoice.setInvoice_date(invoiceDto.getInvoice_date());
            invoice.setOrders(order);
            invoice.setInvoice_amount(order.getOrder_price());

            Invoice savedInvoice = invoiceRepo.save(invoice);
            return invoiceMapper.invoiceToInvoiceDto(savedInvoice);
        }

        throw new RuntimeException("Order not found with ID: " + invoiceDto.getOrderId());
    }


    @Override
    public InvoiceDto updateInvoice(InvoiceDto invoiceDto) {
        Optional<Invoice> invoiceOpt = invoiceRepo.findById(invoiceDto.getInvoice_id());

        if (invoiceOpt.isPresent()) {
            Invoice existingInvoice = invoiceOpt.get();

            existingInvoice.setInvoice_date(invoiceDto.getInvoice_date());

            Optional<Orders> orderOpt = orderRepo.findById(invoiceDto.getOrderId());
            orderOpt.ifPresent(order -> {
                existingInvoice.setOrders(order);
                existingInvoice.setInvoice_amount(order.getOrder_price());
            });

            Invoice updatedInvoice = invoiceRepo.save(existingInvoice);
            return invoiceMapper.invoiceToInvoiceDto(updatedInvoice);
        }

        return null;
    }

    @Override
    public InvoiceDto deleteInvoice(Integer invoiceId) {
        Optional<Invoice> invoiceOpt = invoiceRepo.findById(invoiceId);

        if (invoiceOpt.isPresent()) {
            invoiceRepo.deleteById(invoiceId);
            return invoiceMapper.invoiceToInvoiceDto(invoiceOpt.get());
        }

        return null;
    }

    @Override
    public Page<InvoiceDto> getAllInvoices(Pageable pageable) {
        Page<Invoice> invoicePage = invoiceRepo.findAll(pageable);
        return invoicePage.map(invoiceMapper::invoiceToInvoiceDto);
    }

    @Override
    public List<InvoiceDto> searchInvoices(String query) {
        List<Invoice> invoices = invoiceRepo.searchInvoices(query);
        return invoices.stream()
                .map(invoiceMapper::invoiceToInvoiceDto)
                .collect(Collectors.toList());
    }
}
