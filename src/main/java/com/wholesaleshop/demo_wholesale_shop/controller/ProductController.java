package com.wholesaleshop.demo_wholesale_shop.controller;

import com.wholesaleshop.demo_wholesale_shop.dto.ProductDto;
import com.wholesaleshop.demo_wholesale_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing products.
 * Base URL: /api/v1/product
 */
@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    @Autowired
    ProductService productService;

    /**
     * Create and save a new product.
     *
     * @param productDto Product data to be saved.
     * @return Saved product.
     */
    @PostMapping
    public ResponseEntity<ProductDto> saveProduct(@RequestBody ProductDto productDto) {
        ProductDto savedProduct = productService.saveProduct(productDto);
        return ResponseEntity.ok(savedProduct);
    }

    /**
     * Update an existing product by ID.
     *
     * @param productDto Updated product data.
     * @param id         Product ID.
     * @return Updated product or 404 if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto, @PathVariable Integer id) {
        productDto.setProduct_id(id);
        ProductDto updatedProduct = productService.updateProduct(productDto);

        if(updatedProduct == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedProduct);
        }
    }

    /**
     * Delete a product by ID.
     *
     * @param id Product ID.
     * @return Deleted product or 404 if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable Integer id) {
        ProductDto deletedProduct = productService.deleteProduct(id);

        if(deletedProduct == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(deletedProduct);
        }
    }

    /**
     * Retrieve all products with pagination.
     *
     * @param page Page number (default is 0).
     * @param size Page size (default is 5).
     * @return Paginated list of products.
     */
    @GetMapping
    public ResponseEntity<Page<ProductDto>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDto> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    /**
     * Search products by keyword.
     *
     * @param query Keyword to search for.
     * @return List of matching products or 204 if none found.
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProducts(@RequestParam String query) {
        List<ProductDto> products = productService.searchProducts(query);

        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(products);
    }
}
