package com.wholesaleshop.demo_wholesale_shop.service.impl;

import com.wholesaleshop.demo_wholesale_shop.dto.PaymentDto;
import com.wholesaleshop.demo_wholesale_shop.entity.Invoice;
import com.wholesaleshop.demo_wholesale_shop.entity.Orders;
import com.wholesaleshop.demo_wholesale_shop.entity.Payment;
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

        // Fetch the invoice by ID and associate it with the payment
        Optional<Invoice> invoiceOpt = invoiceRepo.findById(paymentDto.getInvoiceId());
        invoiceOpt.ifPresent(payment::setInvoice);

        // Fetch the order by ID and associate it with the payment
        Optional<Orders> orderOpt = orderRepo.findById(paymentDto.getOrderId());
        orderOpt.ifPresent(payment::setOrders);

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
        // Fetch the existing payment by ID
        Optional<Payment> existingPaymentOpt = paymentRepo.findById(paymentDto.getPayment_id());

        if (existingPaymentOpt.isPresent()) {
            Payment existingPayment = existingPaymentOpt.get();

            // Update payment properties
            existingPayment.setPayment_method(paymentDto.getPayment_method());
            existingPayment.setPayment_date(paymentDto.getPayment_date());
            existingPayment.setPaid_amount(paymentDto.getPaid_amount());

            // Fetch the invoice by ID and associate it with the payment
            invoiceRepo.findById(paymentDto.getInvoiceId())
                    .ifPresent(existingPayment::setInvoice);

            // Fetch the order by ID and associate it with the payment
            orderRepo.findById(paymentDto.getOrderId())
                    .ifPresent(existingPayment::setOrders);

            // Save the updated payment and return the updated PaymentDto
            Payment updatedPayment = paymentRepo.save(existingPayment);
            return paymentMapper.paymentToPaymentDto(updatedPayment);
        }

        return null;
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
        // Fetch the payment by ID
        Optional<Payment> paymentOpt = paymentRepo.findById(paymentId);

        if (paymentOpt.isPresent()) {
            // Delete the payment from the repository
            paymentRepo.deleteById(paymentId);

            // Return the deleted payment as PaymentDto
            return paymentMapper.paymentToPaymentDto(paymentOpt.get());
        }

        return null;
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
