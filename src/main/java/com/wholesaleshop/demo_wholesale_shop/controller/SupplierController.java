package com.wholesaleshop.demo_wholesale_shop.controller;

import com.wholesaleshop.demo_wholesale_shop.dto.SupplierDto;
import com.wholesaleshop.demo_wholesale_shop.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @PostMapping
    public ResponseEntity<SupplierDto> saveSupplier(@RequestBody SupplierDto supplierDto) {
        SupplierDto savedSupplier = supplierService.saveSupplier(supplierDto);
        return ResponseEntity.ok(savedSupplier);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierDto> updateSupplier(@RequestBody SupplierDto supplierDto, @PathVariable Integer id) {
        supplierDto.setSupplier_id(id);
        SupplierDto updatedSupplier = supplierService.updateSupplier(id, supplierDto);

        if (updatedSupplier == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedSupplier);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SupplierDto> deleteSupplier(@PathVariable Integer id) {
        SupplierDto deletedSupplier = supplierService.deleteSupplier(id);

        if (deletedSupplier == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(deletedSupplier);
        }
    }

    @GetMapping
    public ResponseEntity<Page<SupplierDto>> getAllSuppliers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SupplierDto> suppliers = supplierService.getAllSuppliers(pageable);
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SupplierDto>> searchSuppliers(@RequestParam String query) {
        List<SupplierDto> suppliers = supplierService.searchSuppliers(query);
        if (suppliers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(suppliers);
    }
}
