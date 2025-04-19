package com.wholesaleshop.demo_wholesale_shop.service;

import com.wholesaleshop.demo_wholesale_shop.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ProductService {
    ProductDto saveProduct(ProductDto productDto);
    ProductDto updateProduct(ProductDto productDto);
    ProductDto deleteProduct(Integer productId);
    Page<ProductDto> getAllProducts(Pageable pageable);
    List<ProductDto> searchProducts(String query);
}
