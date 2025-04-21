package com.wholesaleshop.demo_wholesale_shop.utils;

import com.wholesaleshop.demo_wholesale_shop.dto.ProductDto;
import com.wholesaleshop.demo_wholesale_shop.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto productToProductDto(Product product);

    @Mapping(target = "supplier", ignore = true)
    Product productDtoToProduct(ProductDto productDto);
}
