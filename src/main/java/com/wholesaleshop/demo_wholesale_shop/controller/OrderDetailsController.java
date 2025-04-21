package com.wholesaleshop.demo_wholesale_shop.controller;

import com.wholesaleshop.demo_wholesale_shop.dto.OrderDetailsDto;
import com.wholesaleshop.demo_wholesale_shop.service.OrderDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing order details.
 * Base URL: /api/order-details
 */
@RestController
@RequestMapping("/api/order-details")
@RequiredArgsConstructor
public class OrderDetailsController {

    @Autowired
    OrderDetailsService orderDetailsService;

    /**
     * Create and save a new order detail entry.
     *
     * @param orderDetailsDto DTO containing order details.
     * @return Created OrderDetailsDto.
     */
    @PostMapping
    public ResponseEntity<OrderDetailsDto> createOrderDetails(@RequestBody OrderDetailsDto orderDetailsDto) {
        OrderDetailsDto createdDto = orderDetailsService.saveOrderDetails(orderDetailsDto);
        return ResponseEntity.ok(createdDto);
    }

    /**
     * Retrieve all order details with pagination.
     *
     * @param page Page number (default is 0).
     * @param size Page size (default is 5).
     * @return Paginated list of order details.
     */
    @GetMapping
    public ResponseEntity<Page<OrderDetailsDto>> getAllOrderDetails(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<OrderDetailsDto> orderDetails = orderDetailsService.getAllOrderDetails(pageable);
        return ResponseEntity.ok(orderDetails);
    }

    /**
     * Update an existing order detail by ID.
     *
     * @param id  ID of the order detail to update.
     * @param dto Updated order detail DTO.
     * @return Updated OrderDetailsDto.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderDetailsDto> updateOrderDetails(@PathVariable Integer id, @RequestBody OrderDetailsDto dto) {
        OrderDetailsDto updated = orderDetailsService.updateOrderDetails(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete an order detail by ID.
     *
     * @param id ID of the order detail to delete.
     * @return Empty response with 200 OK.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderDetails(@PathVariable Integer id) {
        orderDetailsService.deleteOrderDetails(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Search for order details by query.
     *
     * @param query Search keyword.
     * @return List of matching order details or 204 No Content if none found.
     */
    @GetMapping("/search")
    public ResponseEntity<List<OrderDetailsDto>> searchOrderDetails(@RequestParam String query) {
        List<OrderDetailsDto> results = orderDetailsService.searchOrderDetails(query);

        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(results);
    }
}
