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
    PaymentRepo paymentRepo;

    @Autowired
    InvoiceRepo invoiceRepo;

    @Autowired
    PaymentMapper paymentMapper;

    @Autowired
    OrderRepo orderRepo;

    @Override
    public PaymentDto savePayment(PaymentDto paymentDto) {
        Payment payment = paymentMapper.paymentDtoToPayment(paymentDto);

        Optional<Invoice> invoiceOpt = invoiceRepo.findById(paymentDto.getInvoiceId());
        invoiceOpt.ifPresent(payment::setInvoice);

        Optional<Orders> orderOpt = orderRepo.findById(paymentDto.getOrderId());
        orderOpt.ifPresent(payment::setOrders);

        Payment savedPayment = paymentRepo.save(payment);
        return paymentMapper.paymentToPaymentDto(savedPayment);
    }


    @Override
    public PaymentDto updatePayment(PaymentDto paymentDto) {
        Optional<Payment> existingPaymentOpt = paymentRepo.findById(paymentDto.getPayment_id());

        if (existingPaymentOpt.isPresent()) {
            Payment existingPayment = existingPaymentOpt.get();

            existingPayment.setPayment_method(paymentDto.getPayment_method());
            existingPayment.setPayment_date(paymentDto.getPayment_date());
            existingPayment.setPaid_amount(paymentDto.getPaid_amount());

            invoiceRepo.findById(paymentDto.getInvoiceId())
                    .ifPresent(existingPayment::setInvoice);

            orderRepo.findById(paymentDto.getOrderId())
                    .ifPresent(existingPayment::setOrders);

            Payment updatedPayment = paymentRepo.save(existingPayment);
            return paymentMapper.paymentToPaymentDto(updatedPayment);
        }

        return null;
    }


    @Override
    public PaymentDto deletePayment(Integer paymentId) {
        Optional<Payment> paymentOpt = paymentRepo.findById(paymentId);

        if (paymentOpt.isPresent()) {
            paymentRepo.deleteById(paymentId);
            return paymentMapper.paymentToPaymentDto(paymentOpt.get());
        }

        return null;
    }

    @Override
    public Page<PaymentDto> getAllPayments(Pageable pageable) {
        Page<Payment> paymentPage = paymentRepo.findAll(pageable);
        return paymentPage.map(paymentMapper::paymentToPaymentDto);
    }

    public List<PaymentDto> searchPayment(String query) {
        List<Payment> payments = paymentRepo.searchPayments(query);
        return payments.stream()
                .map(paymentMapper::paymentToPaymentDto)
                .collect(Collectors.toList());
    }
}
