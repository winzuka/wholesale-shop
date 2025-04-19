package com.wholesaleshop.demo_wholesale_shop.utils;

import com.wholesaleshop.demo_wholesale_shop.dto.SupplierDto;
import com.wholesaleshop.demo_wholesale_shop.entity.Supplier;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupplierMapper {

    // Entity to DTO Mapping
    SupplierDto supplierToSupplierDto(Supplier supplier);

    // DTO to Entity Mapping
    Supplier supplierDtoToSupplier(SupplierDto supplierDto);
}
