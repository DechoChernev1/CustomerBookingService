package com.rewe.customerbookingservice.controllers;

import com.rewe.customerbookingservice.dtos.BrandDTO;
import com.rewe.customerbookingservice.services.BrandService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/brands")
@Validated
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping
    public ResponseEntity<BrandDTO> addBrand(@Valid @RequestBody BrandDTO brand) {
        BrandDTO savedBrand = brandService.saveBrand(brand);
        return new ResponseEntity<>(savedBrand, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandDTO> updateBrand(@PathVariable @Positive Long id,
                                                @Valid @RequestBody BrandDTO brand) {
        BrandDTO updatedBrand = brandService.updateBrand(id, brand);
        return updatedBrand != null ?
                ResponseEntity.ok(updatedBrand) :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable @Positive Long id) {
        boolean isDeleted = brandService.deleteBrand(id);
        return isDeleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
