package com.rewe.customerbookingservice.services.impl;

import com.rewe.customerbookingservice.data.entities.Brand;
import com.rewe.customerbookingservice.data.repositories.BrandRepository;
import com.rewe.customerbookingservice.dtos.BrandDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class BrandServiceImplTest {

    @Mock
    private BrandRepository brandRepository;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private BrandServiceImpl brandService;

    private Brand brand;
    private BrandDTO brandDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        brand = new Brand();
        brand.setId(1L);
        brand.setName("Brand A");
        brandDTO = modelMapper.map(brand, BrandDTO.class);
    }

    @Test
    void saveBrand_shouldReturnSavedBrandDTO_whenBrandIsValid() {
        when(brandRepository.save(any(Brand.class))).thenReturn(brand);

        BrandDTO result = brandService.saveBrand(brandDTO);

        assertNotNull(result);
        assertEquals(brandDTO.getId(), result.getId());
        assertEquals(brandDTO.getName(), result.getName());
    }

    @Test
    void findBrandById_shouldReturnBrandDTO_whenBrandExists() {
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));

        Optional<BrandDTO> result = brandService.findBrandById(1L);

        assertTrue(result.isPresent());
        assertEquals(brandDTO.getId(), result.get().getId());
    }

    @Test
    void deleteBrand_shouldReturnTrue_whenBrandIsDeleted() {
        when(brandRepository.existsById(1L)).thenReturn(false);

        boolean result = brandService.deleteBrand(1L);

        assertTrue(result);
    }

    @Test
    void findAllBrands_shouldReturnListOfBrandDTOs() {
        when(brandRepository.findAll()).thenReturn(List.of(brand));

        List<BrandDTO> result = brandService.findAllBrands();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(brandDTO.getId(), result.get(0).getId());
    }

    @Test
    void updateBrand_shouldReturnUpdatedBrandDTO_whenBrandExists() {
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        when(brandRepository.save(any(Brand.class))).thenReturn(brand);

        BrandDTO updatedDetails = new BrandDTO();
        updatedDetails.setId(1L);
        updatedDetails.setName("Brand B");

        BrandDTO result = brandService.updateBrand(1L, updatedDetails);

        assertNotNull(result);
        assertEquals("Brand B", result.getName());
    }
}
