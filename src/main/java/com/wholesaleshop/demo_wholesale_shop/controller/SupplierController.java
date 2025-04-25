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

/**
 * REST controller for managing suppliers.
 * Base URL: /api/v1/supplier
 */
@RestController
@RequestMapping("/api/v1/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    /**
     * Create and save a new supplier.
     *
     * @param supplierDto Supplier data to save.
     * @return Saved supplier.
     */
    @PostMapping
    public ResponseEntity<SupplierDto> saveSupplier(@RequestBody SupplierDto supplierDto) {
        SupplierDto savedSupplier = supplierService.saveSupplier(supplierDto);
        return ResponseEntity.ok(savedSupplier);
    }

    /**
     * Update an existing supplier by ID.
     *
     * @param supplierDto Updated supplier data.
     * @param id          Supplier ID to update.
     * @return Updated supplier or 404 if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SupplierDto> updateSupplier(@RequestBody SupplierDto supplierDto, @PathVariable Integer id) {
        supplierDto.setSupplier_id(id);
        SupplierDto updatedSupplier = supplierService.updateSupplier(id, supplierDto);

            return ResponseEntity.ok(updatedSupplier);
    }

    /**
     * Delete a supplier by ID.
     *
     * @param id Supplier ID to delete.
     * @return Deleted supplier or 404 if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<SupplierDto> deleteSupplier(@PathVariable Integer id) {
        SupplierDto deletedSupplier = supplierService.deleteSupplier(id);

            return ResponseEntity.ok(deletedSupplier);
    }

    /**
     * Retrieve all suppliers with pagination.
     *
     * @param page Page number (default is 0).
     * @param size Number of records per page (default is 5).
     * @return Paginated list of suppliers.
     */
    @GetMapping
    public ResponseEntity<Page<SupplierDto>> getAllSuppliers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SupplierDto> suppliers = supplierService.getAllSuppliers(pageable);
        return ResponseEntity.ok(suppliers);
    }

    /**
     * Search suppliers by keyword.
     *
     * @param query Keyword for search.
     * @return List of matching suppliers or 204 if none found.
     */
    @GetMapping("/search")
    public ResponseEntity<List<SupplierDto>> searchSuppliers(@RequestParam String query) {
        List<SupplierDto> suppliers = supplierService.searchSuppliers(query);
        if (suppliers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(suppliers);
    }
}
