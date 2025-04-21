package com.wholesaleshop.demo_wholesale_shop.utils;

import com.wholesaleshop.demo_wholesale_shop.dto.SupplierDto;
import com.wholesaleshop.demo_wholesale_shop.entity.Supplier;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupplierMapper {

    SupplierDto supplierToSupplierDto(Supplier supplier);

    Supplier supplierDtoToSupplier(SupplierDto supplierDto);
}
