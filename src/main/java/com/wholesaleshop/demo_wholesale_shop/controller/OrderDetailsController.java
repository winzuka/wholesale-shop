package com.wholesaleshop.demo_wholesale_shop.controller;

import com.wholesaleshop.demo_wholesale_shop.dto.OrderDetailsDto;
import com.wholesaleshop.demo_wholesale_shop.service.OrderDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-details")
@RequiredArgsConstructor
public class OrderDetailsController {

    OrderDetailsService orderDetailsService;

    @PostMapping
    public ResponseEntity<OrderDetailsDto> createOrderDetails(@RequestBody OrderDetailsDto orderDetailsDto) {
        OrderDetailsDto createdDto = orderDetailsService.saveOrderDetails(orderDetailsDto);
        return ResponseEntity.ok(createdDto);
    }

    @GetMapping
    public ResponseEntity<List<OrderDetailsDto>> getAllOrderDetails() {
        return ResponseEntity.ok(orderDetailsService.getAllOrderDetails());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailsDto> getOrderDetailsById(@PathVariable Integer id) {
        return ResponseEntity.ok(orderDetailsService.getOrderDetailsById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDetailsDto> updateOrderDetails(@PathVariable Integer id, @RequestBody OrderDetailsDto dto) {
        OrderDetailsDto updated = orderDetailsService.updateOrderDetails(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderDetails(@PathVariable Integer id) {
        orderDetailsService.deleteOrderDetails(id);
        return ResponseEntity.noContent().build();
    }
}
