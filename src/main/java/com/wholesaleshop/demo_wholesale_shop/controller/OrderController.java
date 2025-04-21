package com.wholesaleshop.demo_wholesale_shop.controller;

import com.wholesaleshop.demo_wholesale_shop.dto.OrderDto;
import com.wholesaleshop.demo_wholesale_shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for handling order-related operations.
 * Base URL: /api/orders
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Create and save a new order.
     *
     * @param orderDto DTO containing order details.
     * @return Saved order DTO.
     */
    @PostMapping
    public OrderDto saveOrder(@RequestBody OrderDto orderDto) {
        return orderService.saveOrder(orderDto);
    }

    /**
     * Update an existing order by ID.
     *
     * @param id       ID of the order to update.
     * @param orderDto Updated order details.
     * @return ResponseEntity with updated order or 404 if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable Integer id, @RequestBody OrderDto orderDto) {
        orderDto.setOrderId(id);
        OrderDto updatedOrder = orderService.updateOrder(orderDto);

        if (updatedOrder == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedOrder);
        }
    }

    /**
     * Delete an order by ID.
     *
     * @param orderId ID of the order to delete.
     * @return Deleted order DTO.
     */
    @DeleteMapping("/{orderId}")
    public OrderDto deleteOrder(@PathVariable Integer orderId) {
        return orderService.deleteOrder(orderId);
    }

    /**
     * Get a paginated list of all orders.
     *
     * @param page Page number (default is 0).
     * @param size Page size (default is 5).
     * @return Paginated list of orders.
     */
    @GetMapping
    public ResponseEntity<Page<OrderDto>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<OrderDto> orders = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(orders);
    }

    /**
     * Search for orders by keyword.
     *
     * @param query Search query.
     * @return List of matching orders or 204 if none found.
     */
    @GetMapping("/search")
    public ResponseEntity<List<OrderDto>> searchOrders(@RequestParam String query) {
        List<OrderDto> orders = orderService.searchOrders(query);

        if (orders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orders);
    }
}

