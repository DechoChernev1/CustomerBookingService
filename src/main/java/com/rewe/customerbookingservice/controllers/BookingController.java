package com.rewe.customerbookingservice.controllers;

import com.rewe.customerbookingservice.dtos.BookingDTO;
import com.rewe.customerbookingservice.dtos.BrandDTO;
import com.rewe.customerbookingservice.services.BookingService;
import com.rewe.customerbookingservice.services.BrandService;
import com.rewe.customerbookingservice.services.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
@Validated
public class BookingController {

    private final BookingService bookingService;
    private final CustomerService customerService;
    private final BrandService brandService;

    public BookingController(BookingService bookingService, CustomerService customerService, BrandService brandService) {
        this.bookingService = bookingService;
        this.customerService = customerService;
        this.brandService = brandService;
    }

    @PostMapping
    public ResponseEntity<BookingDTO> addBooking(@Valid @RequestBody BookingDTO booking) {
        BookingDTO savedBooking = bookingService.saveBooking(booking);
        return new ResponseEntity<>(savedBooking, HttpStatus.CREATED);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<BookingDTO>> getBookingsForCustomer(@PathVariable @Positive Long customerId) {
        List<BookingDTO> bookings = bookingService.findBookingsByCustomerId(customerId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/brand/{brandId}")
    public ResponseEntity<List<BookingDTO>> getBookingsByBrand(@PathVariable @Positive Long brandId) {
        List<BookingDTO> bookings = bookingService.findBookingsByBrandId(brandId);
        return ResponseEntity.ok(bookings);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable @Positive Long id) {
        boolean isDeleted = bookingService.deleteBooking(id);
        return isDeleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/addBrand/{bookingId}/{brandId}")
    public ResponseEntity<BookingDTO> addBrandToBooking(@PathVariable @Positive Long bookingId,
                                                        @PathVariable @Positive Long brandId) {
        Optional<BookingDTO> bookingDTO = bookingService.findBookingById(bookingId);
        if (bookingDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<BrandDTO> brandDTO = brandService.findBrandById(brandId);
        if (brandDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        bookingDTO.get().setBrand(brandDTO.get());
        BookingDTO updatedBooking = bookingService.updateBooking(bookingId, bookingDTO.get());
        return updatedBooking != null ? ResponseEntity.ok(updatedBooking) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable @Positive Long id) {
        Optional<BookingDTO> booking = bookingService.findBookingById(id);
        return booking.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingDTO> updateBooking(@PathVariable @Positive Long id,
                                                    @Valid @RequestBody BookingDTO bookingDetails) {
        BookingDTO updatedBooking = bookingService.updateBooking(id, bookingDetails);
        return updatedBooking != null ? ResponseEntity.ok(updatedBooking) : ResponseEntity.notFound().build();
    }
}
