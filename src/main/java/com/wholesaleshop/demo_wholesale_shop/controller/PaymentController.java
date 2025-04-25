package com.wholesaleshop.demo_wholesale_shop.controller;

import com.wholesaleshop.demo_wholesale_shop.dto.PaymentDto;
import com.wholesaleshop.demo_wholesale_shop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing payments.
 * Base URL: /api/v1/payment
 */
@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    /**
     * Create and save a new payment.
     *
     * @param paymentDto Payment details to save.
     * @return Saved payment.
     */
    @PostMapping
    public ResponseEntity<PaymentDto> savePayment(@RequestBody PaymentDto paymentDto) {
        PaymentDto savedPayment = paymentService.savePayment(paymentDto);
        return ResponseEntity.ok(savedPayment);
    }

    /**
     * Update an existing payment by ID.
     *
     * @param paymentDto Updated payment details.
     * @param id         Payment ID.
     * @return Updated payment or 404 if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentDto> updatePayment(@RequestBody PaymentDto paymentDto, @PathVariable Integer id) {
        paymentDto.setPayment_id(id);
        PaymentDto updatedPayment = paymentService.updatePayment(paymentDto);

            return ResponseEntity.ok(updatedPayment);
    }

    /**
     * Delete a payment by ID.
     *
     * @param id Payment ID.
     * @return Deleted payment or 404 if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<PaymentDto> deletePayment(@PathVariable Integer id) {
        PaymentDto deletedPayment = paymentService.deletePayment(id);

            return ResponseEntity.ok(deletedPayment);
    }

    /**
     * Get all payments with pagination.
     *
     * @param page Page number (default 0).
     * @param size Page size (default 5).
     * @return Paginated list of payments.
     */
    @GetMapping
    public ResponseEntity<Page<PaymentDto>> getAllPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PaymentDto> payments = paymentService.getAllPayments(pageable);
        return ResponseEntity.ok(payments);
    }

    /**
     * Search payments by a query string.
     *
     * @param query Search keyword.
     * @return List of matching payments or 204 if no content.
     */
    @GetMapping("/search")
    public ResponseEntity<List<PaymentDto>> searchPayments(@RequestParam String query) {
        List<PaymentDto> payments = paymentService.searchPayment(query);

        if (payments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(payments);
    }
}
