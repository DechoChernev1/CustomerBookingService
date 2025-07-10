package com.rewe.customerbookingservice.services.impl;

import com.rewe.customerbookingservice.data.entities.Brand;
import com.rewe.customerbookingservice.data.repositories.BrandRepository;
import com.rewe.customerbookingservice.dtos.BookingDTO;
import com.rewe.customerbookingservice.dtos.BrandDTO;
import jakarta.persistence.EntityNotFoundException;
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
    void deleteBrand_shouldReturnTrue_whenBrandIsDeleted() {
        when(brandRepository.existsById(1L)).thenReturn(false);

        boolean result = brandService.deleteBrand(1L);

        assertTrue(result);
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

    @Test
    void updateBrand_shouldReturnException_whenBrandDoesNotExists() {
        when(brandRepository.findById(1L)).thenReturn(Optional.empty());

        BrandDTO updatedDetails = new BrandDTO();
        updatedDetails.setId(1L);
        updatedDetails.setName("Brand B");

        // Act and Assert
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> brandService.updateBrand(1L, updatedDetails)
        );

        assertEquals("Brand not found for id: 1", exception.getMessage());
    }
}
