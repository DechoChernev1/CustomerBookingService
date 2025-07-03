package com.rewe.customerbookingservice.services.impl;

import com.rewe.customerbookingservice.data.entities.Brand;
import com.rewe.customerbookingservice.data.repositories.BrandRepository;
import com.rewe.customerbookingservice.dtos.BrandDTO;
import com.rewe.customerbookingservice.services.BrandService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BrandServiceImpl(BrandRepository brandRepository, ModelMapper modelMapper) {
        this.brandRepository = brandRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<BrandDTO> findAllBrands() {
        return brandRepository.findAll()
                .stream()
                .map(brand -> modelMapper.map(brand, BrandDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BrandDTO> findBrandById(Long id) {
        return brandRepository.findById(id)
                .map(brand -> modelMapper.map(brand, BrandDTO.class));
    }

    @Override
    public BrandDTO saveBrand(BrandDTO brandDTO) {
        Brand brand = modelMapper.map(brandDTO, Brand.class);
        Brand savedBrand = brandRepository.save(brand);
        return modelMapper.map(savedBrand, BrandDTO.class);
    }

    @Override
    public BrandDTO updateBrand(Long id, BrandDTO brandDetails) {
        Optional<Brand> existingBrand = brandRepository.findById(id);
        if (existingBrand.isPresent()) {
            Brand brandToUpdate = existingBrand.get();
            brandToUpdate.setName(brandDetails.getName());
            brandToUpdate.setAddress(brandDetails.getAddress());
            brandToUpdate.setShortCode(brandDetails.getShortCode());
            Brand updatedBrand = brandRepository.save(brandToUpdate);
            return modelMapper.map(updatedBrand, BrandDTO.class);
        }
        return null; // Or throw an exception about the record not being found
    }

    @Override
    public boolean deleteBrand(Long id) {
        brandRepository.deleteById(id);
        return !brandRepository.existsById(id);
    }
}
