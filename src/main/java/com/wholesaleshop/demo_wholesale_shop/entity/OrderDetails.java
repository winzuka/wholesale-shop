package com.wholesaleshop.demo_wholesale_shop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer orderDetailsId;
    Integer quantity;
    Double subtotal;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    Order order;

}
