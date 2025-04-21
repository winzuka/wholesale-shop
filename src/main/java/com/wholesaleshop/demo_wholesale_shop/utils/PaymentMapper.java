package com.wholesaleshop.demo_wholesale_shop.utils;

import com.wholesaleshop.demo_wholesale_shop.dto.PaymentDto;
import com.wholesaleshop.demo_wholesale_shop.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "invoice.invoice_id", target = "invoiceId")
    @Mapping(source = "orders.orderId", target = "orderId")
    PaymentDto paymentToPaymentDto(Payment payment);

    @Mapping(source = "invoiceId", target = "invoice.invoice_id")
    @Mapping(source = "orderId", target = "orders.orderId")
    Payment paymentDtoToPayment(PaymentDto paymentDto);
}
