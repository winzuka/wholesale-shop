package com.wholesaleshop.demo_wholesale_shop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private
    Integer invoice_id;
    private LocalDate invoice_date;
    private Double invoice_amount;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders orders;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<Payment> payments;

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

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
}
