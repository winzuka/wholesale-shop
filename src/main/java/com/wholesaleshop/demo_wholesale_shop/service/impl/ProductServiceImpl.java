package com.wholesaleshop.demo_wholesale_shop.service.impl;

import com.wholesaleshop.demo_wholesale_shop.dto.ProductDto;
import com.wholesaleshop.demo_wholesale_shop.entity.Product;
import com.wholesaleshop.demo_wholesale_shop.entity.Supplier;
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
    ProductRepo productRepo;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    SupplierRepo supplierRepo;

    @Override
    public ProductDto saveProduct(ProductDto productDto) {

        Supplier supplier = supplierRepo.findById(Long.valueOf(productDto.getSupplier_id()))
                .orElseThrow(() -> new RuntimeException("Supplier not found with id " + productDto.getSupplier_id()));

        Product product = productMapper.productDtoToProduct(productDto);
        product.setSupplier(supplier);

        product.setOrderDetails(new ArrayList<>());

        Product savedProduct = productRepo.save(product);
        return productMapper.productToProductDto(savedProduct);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        Optional<Product> existingProductOpt = productRepo.findById(productDto.getProduct_id());

        if (existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();
            existingProduct.setProduct_name(productDto.getProduct_name());
            existingProduct.setProduct_price(productDto.getProduct_price());
            existingProduct.setStock_quantity(productDto.getStock_quantity());

            Product updatedProduct = productRepo.save(existingProduct);
            return productMapper.productToProductDto(updatedProduct);
        }

        return null; // Handle error case properly in the controller
    }

    @Override
    public ProductDto deleteProduct(Integer productId) {
        Optional<Product> productOpt = productRepo.findById(productId);

        if (productOpt.isPresent()) {
            productRepo.deleteById(productId);
            return productMapper.productToProductDto(productOpt.get());
        }

        return null; // Handle error case properly in the controller
    }

    @Override
    public Page<ProductDto> getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepo.findAll(pageable);
        return productPage.map(productMapper::productToProductDto);
    }

    @Override
    public List<ProductDto> searchProducts(String query) {
        List<Product> products = productRepo.searchProducts(query);
        return products.stream()
                .map(productMapper::productToProductDto)
                .collect(Collectors.toList());
    }
}
