package com.wholesaleshop.demo_wholesale_shop.service.impl;

import com.wholesaleshop.demo_wholesale_shop.dto.InvoiceDto;
import com.wholesaleshop.demo_wholesale_shop.entity.Invoice;
import com.wholesaleshop.demo_wholesale_shop.entity.Orders;
import com.wholesaleshop.demo_wholesale_shop.repo.InvoiceRepo;
import com.wholesaleshop.demo_wholesale_shop.repo.OrderRepo;
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
    InvoiceRepo invoiceRepo; // Repository to interact with the Invoice data
    @Autowired
    InvoiceMapper invoiceMapper; // Mapper to convert between Invoice DTO and entity
    @Autowired
    OrderRepo orderRepo; // Repository to interact with the Order data

    /**
     * Save a new invoice.
     * @param invoiceDto DTO containing invoice data.
     * @return The saved InvoiceDto.
     */
    @Override
    public InvoiceDto saveInvoice(InvoiceDto invoiceDto) {
        // Fetch the order by its ID to associate it with the invoice.
        Optional<Orders> orderOpt = orderRepo.findById(invoiceDto.getOrderId());

        if (orderOpt.isPresent()) {
            Orders order = orderOpt.get();

            // Create a new Invoice entity and set the properties.
            Invoice invoice = new Invoice();
            invoice.setInvoice_date(invoiceDto.getInvoice_date());
            invoice.setOrders(order);
            invoice.setInvoice_amount(order.getOrder_price());

            // Save the invoice in the database
            Invoice savedInvoice = invoiceRepo.save(invoice);

            // Convert the saved invoice entity to DTO and return.
            return invoiceMapper.invoiceToInvoiceDto(savedInvoice);
        }

        throw new RuntimeException("Order not found with ID: " + invoiceDto.getOrderId());
    }

    /**
     * Update an existing invoice's details.
     * @param invoiceDto DTO containing updated invoice data.
     * @return The updated InvoiceDto, or null if the invoice doesn't exist.
     */
    @Override
    public InvoiceDto updateInvoice(InvoiceDto invoiceDto) {
        // Fetch the existing invoice by its ID.
        Optional<Invoice> invoiceOpt = invoiceRepo.findById(invoiceDto.getInvoice_id());

        if (invoiceOpt.isPresent()) {
            Invoice existingInvoice = invoiceOpt.get();

            // Update the invoice details.
            existingInvoice.setInvoice_date(invoiceDto.getInvoice_date());

            // Fetch the associated order and update the invoice's order and amount.
            Optional<Orders> orderOpt = orderRepo.findById(invoiceDto.getOrderId());
            orderOpt.ifPresent(order -> {
                existingInvoice.setOrders(order);
                existingInvoice.setInvoice_amount(order.getOrder_price());
            });

            // Save the updated invoice.
            Invoice updatedInvoice = invoiceRepo.save(existingInvoice);

            // Convert the updated invoice entity to DTO and return.
            return invoiceMapper.invoiceToInvoiceDto(updatedInvoice);
        }

        return null;
    }

    /**
     * Delete an invoice by its ID.
     * @param invoiceId ID of the invoice to be deleted.
     * @return The deleted InvoiceDto, or null if the invoice doesn't exist.
     */
    @Override
    public InvoiceDto deleteInvoice(Integer invoiceId) {
        // Fetch the invoice by its ID to check if it exists.
        Optional<Invoice> invoiceOpt = invoiceRepo.findById(invoiceId);

        if (invoiceOpt.isPresent()) {
            // Delete the invoice from the database.
            invoiceRepo.deleteById(invoiceId);

            // Convert the deleted invoice entity to DTO and return.
            return invoiceMapper.invoiceToInvoiceDto(invoiceOpt.get());
        }

        return null;
    }

    /**
     * Get all invoices with pagination.
     * @param pageable Pageable object containing pagination information.
     * @return A page of InvoiceDto objects.
     */
    @Override
    public Page<InvoiceDto> getAllInvoices(Pageable pageable) {
        // Fetch the invoices from the database with pagination.
        Page<Invoice> invoicePage = invoiceRepo.findAll(pageable);

        // Convert the entities in the page to DTOs and return the page.
        return invoicePage.map(invoiceMapper::invoiceToInvoiceDto);
    }

    /**
     * Search invoices based on a query.
     * @param query The search query to filter invoices.
     * @return A list of InvoiceDto objects that match the query.
     */
    @Override
    public List<InvoiceDto> searchInvoices(String query) {
        // Fetch the list of invoices based on the query.
        List<Invoice> invoices = invoiceRepo.searchInvoices(query);

        // Convert the list of entities to DTOs and return.
        return invoices.stream()
                .map(invoiceMapper::invoiceToInvoiceDto)
                .collect(Collectors.toList());
    }
}
