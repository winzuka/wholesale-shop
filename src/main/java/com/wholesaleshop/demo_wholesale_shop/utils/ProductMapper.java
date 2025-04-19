package com.wholesaleshop.demo_wholesale_shop.utils;

import com.wholesaleshop.demo_wholesale_shop.dto.ProductDto;
import com.wholesaleshop.demo_wholesale_shop.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    // Entity to DTO Mapping
    ProductDto productToProductDto(Product product);

    // DTO to Entity Mapping
    @Mapping(target = "supplier", ignore = true)  // We ignore the Supplier entity, since we only deal with supplier_id
    Product productDtoToProduct(ProductDto productDto);
}
