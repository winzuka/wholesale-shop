package com.wholesaleshop.demo_wholesale_shop.service.impl;

import com.wholesaleshop.demo_wholesale_shop.dto.ProductDto;
import com.wholesaleshop.demo_wholesale_shop.entity.Product;
import com.wholesaleshop.demo_wholesale_shop.entity.Supplier;
import com.wholesaleshop.demo_wholesale_shop.exception.ResourceNotFoundException;
import com.wholesaleshop.demo_wholesale_shop.repo.ProductRepo;
import com.wholesaleshop.demo_wholesale_shop.repo.SupplierRepo;
import com.wholesaleshop.demo_wholesale_shop.service.ProductService;
import com.wholesaleshop.demo_wholesale_shop.utils.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo productRepo;  // Repository for interacting with Product data

    @Autowired
    private ProductMapper productMapper;  // Mapper to convert between ProductDto and Product entity

    @Autowired
    private SupplierRepo supplierRepo;  // Repository for interacting with Supplier data

    /**
     * Save a new product.
     * - Maps the ProductDto to a Product entity.
     * - Associates the product with the specified supplier.
     * - Initializes the order details for the product.
     *
     * @param productDto the ProductDto to save
     * @return the saved ProductDto
     */
    @Override
    public ProductDto saveProduct(ProductDto productDto) {
        // Fetch the supplier by ID and associate it with the product
        Supplier supplier = supplierRepo.findById(Long.valueOf(productDto.getSupplier_id()))
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id " + productDto.getSupplier_id()));

        // Map ProductDto to Product entity
        Product product = productMapper.productDtoToProduct(productDto);
        // Set the supplier for the product
        product.setSupplier(supplier);

        // Initialize the order details (could be filled later)
        product.setOrderDetails(new ArrayList<>());

        // Save the product and return the saved ProductDto
        Product savedProduct = productRepo.save(product);
        return productMapper.productToProductDto(savedProduct);
    }

    /**
     * Update an existing product.
     * - Updates product details based on the provided ProductDto.
     *
     * @param productDto the ProductDto with updated data
     * @return the updated ProductDto, or null if the product was not found
     */
    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        // Fetch the existing product by ID or throw exception if not found
        Product existingProduct = productRepo.findById(productDto.getProduct_id())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productDto.getProduct_id()));

        // Update product properties
            existingProduct.setProduct_name(productDto.getProduct_name());
            existingProduct.setProduct_price(productDto.getProduct_price());
            existingProduct.setStock_quantity(productDto.getStock_quantity());

            // Save the updated product and return the updated ProductDto
            Product updatedProduct = productRepo.save(existingProduct);
            return productMapper.productToProductDto(updatedProduct);
    }

    /**
     * Delete a product by its ID.
     * - Fetches the product, deletes it, and returns the deleted product as ProductDto.
     *
     * @param productId the ID of the product to delete
     * @return the deleted ProductDto, or null if the product was not found
     */
    @Override
    public ProductDto deleteProduct(Integer productId) {
        // Fetch the product by ID or throw an exception if not found
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

            // Delete the product from the repository
            productRepo.deleteById(productId);

            // Return the deleted product as ProductDto
            return productMapper.productToProductDto(product);
    }

    /**
     * Get all products with pagination.
     *
     * @param pageable the pagination information
     * @return a page of ProductDtos
     */
    @Override
    public Page<ProductDto> getAllProducts(Pageable pageable) {
        // Fetch all products with pagination
        Page<Product> productPage = productRepo.findAll(pageable);

        // Convert Product entities to ProductDtos
        return productPage.map(productMapper::productToProductDto);
    }

    /**
     * Search for products based on a query string.
     *
     * @param query the search query
     * @return a list of matching ProductDtos
     */
    @Override
    public List<ProductDto> searchProducts(String query) {
        // Fetch products that match the search query
        List<Product> products = productRepo.searchProducts(query);

        // Convert the matching products to ProductDtos
        return products.stream()
                .map(productMapper::productToProductDto)
                .collect(Collectors.toList());
    }
}
