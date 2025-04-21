package com.wholesaleshop.demo_wholesale_shop.service.impl;

import com.wholesaleshop.demo_wholesale_shop.dto.SupplierDto;
import com.wholesaleshop.demo_wholesale_shop.entity.Product;
import com.wholesaleshop.demo_wholesale_shop.entity.Supplier;
import com.wholesaleshop.demo_wholesale_shop.repo.SupplierRepo;
import com.wholesaleshop.demo_wholesale_shop.service.SupplierService;
import com.wholesaleshop.demo_wholesale_shop.utils.SupplierMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    SupplierRepo supplierRepo;

    @Autowired
    SupplierMapper supplierMapper;

    @Override
    public SupplierDto saveSupplier(SupplierDto supplierDto) {
        Supplier supplier = supplierMapper.supplierDtoToSupplier(supplierDto);
        Supplier saved = supplierRepo.save(supplier);
        return supplierMapper.supplierToSupplierDto(saved);
    }

    @Override
    public SupplierDto updateSupplier(Integer id,SupplierDto supplierDto) {
        Optional<Supplier> existingSupplierOpt = supplierRepo.findById(Long.valueOf(id));

        if (existingSupplierOpt.isPresent()) {
            Supplier existingSupplier = existingSupplierOpt.get();
            existingSupplier.setSupplier_name(supplierDto.getSupplier_name());
            existingSupplier.setSupplier_phone(supplierDto.getSupplier_phone());
            existingSupplier.setSupplier_address(supplierDto.getSupplier_address());

            Supplier updatedSupplier = supplierRepo.save(existingSupplier);
            return supplierMapper.supplierToSupplierDto(updatedSupplier);
        }

        return null; // Handle error case properly in the controller
    }


    @Override
    public SupplierDto deleteSupplier(Integer id) {
        Optional<Supplier> optionalSupplier = supplierRepo.findById(Long.valueOf(id));

        if (optionalSupplier.isPresent()) {
            supplierRepo.deleteById(Long.valueOf(id));
            return supplierMapper.supplierToSupplierDto(optionalSupplier.get());
        }

        return null;
    }

    @Override
    public Page<SupplierDto> getAllSuppliers(Pageable pageable) {
        return supplierRepo.findAll(pageable)
                .map(supplierMapper::supplierToSupplierDto);
    }

    @Override
    public List<SupplierDto> searchSuppliers(String query) {
        List<Supplier> supplier = supplierRepo.searchSuppliers(query);
        return supplier
                .stream()
                .map(supplierMapper::supplierToSupplierDto)
                .collect(Collectors.toList());
    }
}
