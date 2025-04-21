package com.wholesaleshop.demo_wholesale_shop.service.impl;

import com.wholesaleshop.demo_wholesale_shop.dto.SupplierDto;
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
    private SupplierRepo supplierRepo;  // Repository for interacting with Supplier data

    @Autowired
    private SupplierMapper supplierMapper;  // Mapper to convert between SupplierDto and Supplier entity

    /**
     * Save a new supplier.
     * - Maps the SupplierDto to a Supplier entity.
     * - Saves the supplier entity in the repository.
     *
     * @param supplierDto the SupplierDto to save
     * @return the saved SupplierDto
     */
    @Override
    public SupplierDto saveSupplier(SupplierDto supplierDto) {
        // Map SupplierDto to Supplier entity
        Supplier supplier = supplierMapper.supplierDtoToSupplier(supplierDto);

        // Save the supplier and return the saved SupplierDto
        Supplier saved = supplierRepo.save(supplier);
        return supplierMapper.supplierToSupplierDto(saved);
    }

    /**
     * Update an existing supplier.
     * - Fetches the existing supplier by ID and updates its properties based on the provided SupplierDto.
     *
     * @param id the ID of the supplier to update
     * @param supplierDto the SupplierDto with updated data
     * @return the updated SupplierDto, or null if the supplier was not found
     */
    @Override
    public SupplierDto updateSupplier(Integer id, SupplierDto supplierDto) {
        // Fetch the existing supplier by ID
        Optional<Supplier> existingSupplierOpt = supplierRepo.findById(Long.valueOf(id));

        if (existingSupplierOpt.isPresent()) {
            Supplier existingSupplier = existingSupplierOpt.get();

            // Update supplier properties
            existingSupplier.setSupplier_name(supplierDto.getSupplier_name());
            existingSupplier.setSupplier_phone(supplierDto.getSupplier_phone());
            existingSupplier.setSupplier_address(supplierDto.getSupplier_address());

            // Save the updated supplier and return the updated SupplierDto
            Supplier updatedSupplier = supplierRepo.save(existingSupplier);
            return supplierMapper.supplierToSupplierDto(updatedSupplier);
        }

        return null;
    }

    /**
     * Delete a supplier by its ID.
     * - Fetches the supplier, deletes it, and returns the deleted supplier as SupplierDto.
     *
     * @param id the ID of the supplier to delete
     * @return the deleted SupplierDto, or null if the supplier was not found
     */
    @Override
    public SupplierDto deleteSupplier(Integer id) {
        // Fetch the supplier by ID
        Optional<Supplier> optionalSupplier = supplierRepo.findById(Long.valueOf(id));

        if (optionalSupplier.isPresent()) {
            // Delete the supplier from the repository
            supplierRepo.deleteById(Long.valueOf(id));

            // Return the deleted supplier as SupplierDto
            return supplierMapper.supplierToSupplierDto(optionalSupplier.get());
        }

        return null;
    }

    /**
     * Get all suppliers with pagination.
     *
     * @param pageable the pagination information
     * @return a page of SupplierDtos
     */
    @Override
    public Page<SupplierDto> getAllSuppliers(Pageable pageable) {
        // Fetch all suppliers with pagination
        return supplierRepo.findAll(pageable)
                .map(supplierMapper::supplierToSupplierDto);
    }

    /**
     * Search for suppliers based on a query string.
     *
     * @param query the search query
     * @return a list of matching SupplierDtos
     */
    @Override
    public List<SupplierDto> searchSuppliers(String query) {
        // Fetch suppliers that match the search query
        List<Supplier> suppliers = supplierRepo.searchSuppliers(query);

        // Convert the matching suppliers to SupplierDtos
        return suppliers
                .stream()
                .map(supplierMapper::supplierToSupplierDto)
                .collect(Collectors.toList());
    }
}
