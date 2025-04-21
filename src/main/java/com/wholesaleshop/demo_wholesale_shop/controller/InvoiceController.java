package com.wholesaleshop.demo_wholesale_shop.controller;

import com.wholesaleshop.demo_wholesale_shop.dto.InvoiceDto;
import com.wholesaleshop.demo_wholesale_shop.entity.Invoice;
import com.wholesaleshop.demo_wholesale_shop.entity.Orders;
import com.wholesaleshop.demo_wholesale_shop.repo.OrderRepo;
import com.wholesaleshop.demo_wholesale_shop.service.InvoiceService;
import com.wholesaleshop.demo_wholesale_shop.utils.InvoiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for handling invoice-related operations.
 * Base URL: /api/v1/invoice
 */
@RestController
@RequestMapping("/api/v1/invoice")
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;
    @Autowired
    OrderRepo orderRepo;

    /**
     * Create a new invoice.
     *
     * Sets the invoice amount based on the associated order's price.
     *
     * @param invoiceDto DTO containing invoice details.
     * @return ResponseEntity with the saved invoice or 400 if order not found.
     */
    @PostMapping
    public ResponseEntity<InvoiceDto> saveInvoice(@RequestBody InvoiceDto invoiceDto) {

        Optional<Orders> orderOpt = orderRepo.findById(invoiceDto.getOrderId());

        if (orderOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        Orders order = orderOpt.get();
        invoiceDto.setInvoice_amount(order.getOrder_price());
        InvoiceDto savedInvoice = invoiceService.saveInvoice(invoiceDto);
        return ResponseEntity.ok(savedInvoice);
    }

    /**
     * Update an existing invoice by ID.
     *
     * @param invoiceDto Updated invoice details.
     * @param id         ID of the invoice to update.
     * @return ResponseEntity with updated invoice or 404 if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceDto> updateInvoice(@RequestBody InvoiceDto invoiceDto, @PathVariable Integer id) {
        invoiceDto.setInvoice_id(id);
        InvoiceDto updatedInvoice = invoiceService.updateInvoice(invoiceDto);

        if (updatedInvoice == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedInvoice);
        }
    }

    /**
     * Delete an invoice by ID.
     *
     * @param id ID of the invoice to delete.
     * @return ResponseEntity with deleted invoice or 404 if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<InvoiceDto> deleteInvoice(@PathVariable Integer id) {
        InvoiceDto deletedInvoice = invoiceService.deleteInvoice(id);

        if (deletedInvoice == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(deletedInvoice);
        }
    }

    /**
     * Get a paginated list of all invoices.
     *
     * @param page Page number (default is 0).
     * @param size Page size (default is 5).
     * @return Paginated list of invoices.
     */
    @GetMapping
    public ResponseEntity<Page<InvoiceDto>> getAllInvoices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<InvoiceDto> invoices = invoiceService.getAllInvoices(pageable);
        return ResponseEntity.ok(invoices);
    }

    /**
     * Search for invoices by query.
     *
     * @param query Keyword to search invoices.
     * @return List of matching invoices or 204 if no results found.
     */
    @GetMapping("/search")
    public ResponseEntity<List<InvoiceDto>> searchInvoices(@RequestParam String query) {
        List<InvoiceDto> invoices = invoiceService.searchInvoices(query);

        if (invoices.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(invoices);
    }
}
