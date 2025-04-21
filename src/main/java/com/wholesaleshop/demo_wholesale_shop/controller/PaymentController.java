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

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentDto> savePayment(@RequestBody PaymentDto paymentDto) {
        PaymentDto savedPayment = paymentService.savePayment(paymentDto);
        return ResponseEntity.ok(savedPayment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDto> updatePayment(@RequestBody PaymentDto paymentDto, @PathVariable Integer id) {
        paymentDto.setPayment_id(id);
        PaymentDto updatedPayment = paymentService.updatePayment(paymentDto);

        if (updatedPayment == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedPayment);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PaymentDto> deletePayment(@PathVariable Integer id) {
        PaymentDto deletedPayment = paymentService.deletePayment(id);

        if (deletedPayment == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(deletedPayment);
        }
    }

    @GetMapping
    public ResponseEntity<Page<PaymentDto>> getAllPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PaymentDto> payments = paymentService.getAllPayments(pageable);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PaymentDto>> searchPayments(@RequestParam String query) {
        List<PaymentDto> payments = paymentService.searchPayment(query);

        if (payments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(payments);
    }
}
