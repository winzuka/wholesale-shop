package com.wholesaleshop.demo_wholesale_shop.utils;

import com.wholesaleshop.demo_wholesale_shop.dto.InvoiceDto;
import com.wholesaleshop.demo_wholesale_shop.entity.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    @Mapping(source = "orders.orderId", target = "orderId")
//    @Mapping(source = "payments.payment_id", target = "paymentId")
    InvoiceDto invoiceToInvoiceDto(Invoice invoice);

    @Mapping(target = "orders.orderId", source = "orderId")
//    @Mapping(target = "payments.payment_id", source = "paymentId")
    Invoice invoiceDtoToInvoice(InvoiceDto invoiceDto);

    @Mapping(target = "invoice_id", ignore = true)
    void updateInvoiceFromDto(InvoiceDto dto, @MappingTarget Invoice entity);
}
