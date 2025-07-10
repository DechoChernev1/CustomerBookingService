package com.rewe.customerbookingservice.controllers;

import com.rewe.customerbookingservice.dtos.BookingDTO;
import com.rewe.customerbookingservice.services.BookingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingDTO> addBooking(@Valid @RequestBody BookingDTO booking) {
        BookingDTO savedBooking = bookingService.saveBooking(booking);
        return new ResponseEntity<>(savedBooking, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteBooking(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(bookingService.deleteBooking(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable @Positive Long id) {
        BookingDTO booking = bookingService.findBookingById(id);
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingDTO> updateBooking(@PathVariable @Positive Long id,
                                                    @Valid @RequestBody BookingDTO bookingDetails) {
        BookingDTO updatedBooking = bookingService.updateBooking(id, bookingDetails);
        return ResponseEntity.ok(updatedBooking);
    }
}
