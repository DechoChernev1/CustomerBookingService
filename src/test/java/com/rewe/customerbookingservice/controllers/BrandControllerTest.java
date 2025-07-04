package com.rewe.customerbookingservice.controllers;

import com.rewe.customerbookingservice.dtos.BrandDTO;
import com.rewe.customerbookingservice.services.BrandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BrandControllerTest {

    @Mock
    private BrandService brandService;

    @InjectMocks
    private BrandController brandController;

    private BrandDTO brandDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        brandDTO = new BrandDTO();
        brandDTO.setId(1L);
        brandDTO.setName("Brand Name");
    }

    @Test
    void testAddBrand() {
        when(brandService.saveBrand(any(BrandDTO.class))).thenReturn(brandDTO);

        ResponseEntity<BrandDTO> response = brandController.addBrand(brandDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(brandDTO, response.getBody());
    }

    @Test
    void testUpdateBrand() {
        when(brandService.updateBrand(eq(1L), any(BrandDTO.class))).thenReturn(brandDTO);

        ResponseEntity<BrandDTO> response = brandController.updateBrand(1L, brandDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(brandDTO, response.getBody());
    }

    @Test
    void testDeleteBrand() {
        when(brandService.deleteBrand(1L)).thenReturn(true);

        ResponseEntity<Void> response = brandController.deleteBrand(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
