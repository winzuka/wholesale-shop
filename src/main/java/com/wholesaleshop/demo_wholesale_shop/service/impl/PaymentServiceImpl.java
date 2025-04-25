package com.wholesaleshop.demo_wholesale_shop.service.impl;

import com.wholesaleshop.demo_wholesale_shop.dto.PaymentDto;
import com.wholesaleshop.demo_wholesale_shop.entity.Invoice;
import com.wholesaleshop.demo_wholesale_shop.entity.Orders;
import com.wholesaleshop.demo_wholesale_shop.entity.Payment;
import com.wholesaleshop.demo_wholesale_shop.exception.ResourceNotFoundException;
import com.wholesaleshop.demo_wholesale_shop.repo.InvoiceRepo;
import com.wholesaleshop.demo_wholesale_shop.repo.OrderRepo;
import com.wholesaleshop.demo_wholesale_shop.repo.PaymentRepo;
import com.wholesaleshop.demo_wholesale_shop.service.PaymentService;
import com.wholesaleshop.demo_wholesale_shop.utils.PaymentMapper;
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
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentRepo paymentRepo; // Repository for interacting with Payment data

    @Autowired
    InvoiceRepo invoiceRepo; // Repository for interacting with Invoice data

    @Autowired
    PaymentMapper paymentMapper; // Mapper to convert between PaymentDto and Payment entity

    @Autowired
    OrderRepo orderRepo; // Repository for interacting with Orders data

    /**
     * Save a new payment.
     * - Maps the PaymentDto to a Payment entity.
     * - Associates the payment with the specified invoice and order.
     *
     * @param paymentDto the PaymentDto to save
     * @return the saved PaymentDto
     */
    @Override
    public PaymentDto savePayment(PaymentDto paymentDto) {
        // Map PaymentDto to Payment entity
        Payment payment = paymentMapper.paymentDtoToPayment(paymentDto);

        // Fetch the invoice by ID or throw exception if not found
        Invoice invoice = invoiceRepo.findById(paymentDto.getInvoiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with ID: " + paymentDto.getInvoiceId()));


        // Fetch the order by ID or throw exception if not found
        Orders order = orderRepo.findById(paymentDto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + paymentDto.getOrderId()));

        // Set invoice and order
        payment.setInvoice(invoice);
        payment.setOrders(order);

        // Save the payment and return the saved PaymentDto
        Payment savedPayment = paymentRepo.save(payment);
        return paymentMapper.paymentToPaymentDto(savedPayment);
    }

    /**
     * Update an existing payment.
     * - Updates payment details based on the provided PaymentDto.
     * - Associates the updated payment with the specified invoice and order.
     *
     * @param paymentDto the PaymentDto with updated data
     * @return the updated PaymentDto, or null if the payment was not found
     */
    @Override
    public PaymentDto updatePayment(PaymentDto paymentDto) {
        // Fetch the existing payment or throw exception if not found
        Payment existingPayment = paymentRepo.findById(paymentDto.getPayment_id())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + paymentDto.getPayment_id()));

            // Update payment properties
            existingPayment.setPayment_method(paymentDto.getPayment_method());
            existingPayment.setPayment_date(paymentDto.getPayment_date());
            existingPayment.setPaid_amount(paymentDto.getPaid_amount());

        // Fetch the invoice or throw if not found
        Invoice invoice = invoiceRepo.findById(paymentDto.getInvoiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with ID: " + paymentDto.getInvoiceId()));
        existingPayment.setInvoice(invoice);

        // Fetch the order or throw if not found
        Orders order = orderRepo.findById(paymentDto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + paymentDto.getOrderId()));
        existingPayment.setOrders(order);

            // Save the updated payment and return the updated PaymentDto
            Payment updatedPayment = paymentRepo.save(existingPayment);
            return paymentMapper.paymentToPaymentDto(updatedPayment);
    }

    /**
     * Delete a payment by its ID.
     * - Fetches the payment, deletes it, and returns the deleted payment as PaymentDto.
     *
     * @param paymentId the ID of the payment to delete
     * @return the deleted PaymentDto, or null if the payment was not found
     */
    @Override
    public PaymentDto deletePayment(Integer paymentId) {
        // Fetch the payment by ID or throw exception if not found
        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + paymentId));

            // Delete the payment from the repository
            paymentRepo.deleteById(paymentId);

            // Return the deleted payment as PaymentDto
            return paymentMapper.paymentToPaymentDto(payment);
    }

    /**
     * Get all payments with pagination.
     *
     * @param pageable the pagination information
     * @return a page of PaymentDtos
     */
    @Override
    public Page<PaymentDto> getAllPayments(Pageable pageable) {
        // Fetch all payments with pagination
        Page<Payment> paymentPage = paymentRepo.findAll(pageable);

        // Convert Payment entities to PaymentDtos
        return paymentPage.map(paymentMapper::paymentToPaymentDto);
    }

    /**
     * Search for payments based on a query string.
     *
     * @param query the search query
     * @return a list of matching PaymentDtos
     */
    @Override
    public List<PaymentDto> searchPayment(String query) {
        // Fetch payments that match the search query
        List<Payment> payments = paymentRepo.searchPayments(query);

        // Convert the matching payments to PaymentDtos
        return payments.stream()
                .map(paymentMapper::paymentToPaymentDto)
                .collect(Collectors.toList());
    }
}
