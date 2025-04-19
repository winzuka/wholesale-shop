package com.wholesaleshop.demo_wholesale_shop.controller;

import com.wholesaleshop.demo_wholesale_shop.dto.OrderDto;
import com.wholesaleshop.demo_wholesale_shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Save new Order
    @PostMapping
    public OrderDto saveOrder(@RequestBody OrderDto orderDto) {
        return orderService.saveOrder(orderDto);
    }

    // Update Order
    @PutMapping
    public OrderDto updateOrder(@RequestBody Integer orderId, OrderDto orderDto) {
        return orderService.updateOrder(orderId,orderDto);
    }

    // Delete Order
    @DeleteMapping("/{orderId}")
    public OrderDto deleteOrder(@PathVariable Integer orderId) {
        return orderService.deleteOrder(orderId);
    }

    // Get all orders with pagination
    @GetMapping
    public Page<OrderDto> getAllOrders(Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }
}

