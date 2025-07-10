package com.rewe.customerbookingservice.controllers;

import com.rewe.customerbookingservice.dtos.BookingDTO;
import com.rewe.customerbookingservice.dtos.BrandDTO;
import com.rewe.customerbookingservice.services.BookingService;
import com.rewe.customerbookingservice.services.BrandService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    private final BrandService brandService;
    private final BookingService bookingService;

    public BrandController(BrandService brandService, BookingService bookingService) {

        this.brandService = brandService;
        this.bookingService = bookingService;
    }

    @GetMapping("/{brandId}/bookings")
    public ResponseEntity<List<BookingDTO>> getBookingsByBrand(@PathVariable @Positive Long brandId) {
        List<BookingDTO> bookings = bookingService.findBookingsByBrandId(brandId);
        return ResponseEntity.ok(bookings);
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
        return ResponseEntity.ok(updatedBrand);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteBrand(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(brandService.deleteBrand(id));
    }
}
