package com.rewe.customerbookingservice.services;

import com.rewe.customerbookingservice.dtos.BrandDTO;

import java.util.List;
import java.util.Optional;

public interface BrandService {

    List<BrandDTO> findAllBrands();

    Optional<BrandDTO> findBrandById(Long id);

    BrandDTO saveBrand(BrandDTO brand);

    BrandDTO updateBrand(Long id, BrandDTO brandDetails);

    boolean deleteBrand(Long id);
}
