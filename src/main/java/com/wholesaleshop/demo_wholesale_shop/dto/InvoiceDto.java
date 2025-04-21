package com.wholesaleshop.demo_wholesale_shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class InvoiceDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer invoice_id;

    private LocalDate invoice_date;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double invoice_amount;

    private Integer orderId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer paymentId;

    public Integer getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(Integer invoice_id) {
        this.invoice_id = invoice_id;
    }

    public LocalDate getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(LocalDate invoice_date) {
        this.invoice_date = invoice_date;
    }

    public Double getInvoice_amount() {
        return invoice_amount;
    }

    public void setInvoice_amount(Double invoice_amount) {
        this.invoice_amount = invoice_amount;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }
}
