package com.wholesaleshop.demo_wholesale_shop.service;

import com.wholesaleshop.demo_wholesale_shop.dto.SupplierDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SupplierService {

    SupplierDto saveSupplier(SupplierDto supplierDto);
    SupplierDto updateSupplier(Integer id, SupplierDto supplierDto);
    SupplierDto deleteSupplier(Integer supplierId);
    Page<SupplierDto> getAllSuppliers(Pageable pageable);
    List<SupplierDto> getAllSuppliers();
}
