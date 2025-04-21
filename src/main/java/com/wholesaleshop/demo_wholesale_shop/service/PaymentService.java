package com.wholesaleshop.demo_wholesale_shop.service;

import com.wholesaleshop.demo_wholesale_shop.dto.PaymentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PaymentService {

    PaymentDto savePayment(PaymentDto paymentDto);
    PaymentDto updatePayment(PaymentDto paymentDto);
    PaymentDto deletePayment(Integer paymentId);
    List<PaymentDto> searchPayment(String query);
    Page<PaymentDto> getAllPayments(Pageable pageable);
}
