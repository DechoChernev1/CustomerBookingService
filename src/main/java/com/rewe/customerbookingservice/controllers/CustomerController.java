package com.rewe.customerbookingservice.controllers;

import com.rewe.customerbookingservice.dtos.BookingDTO;
import com.rewe.customerbookingservice.dtos.CustomerDTO;
import com.rewe.customerbookingservice.services.BookingService;
import com.rewe.customerbookingservice.services.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final BookingService bookingService;

    public CustomerController(CustomerService customerService, BookingService bookingService) {
        this.bookingService = bookingService;
        this.customerService = customerService;
    }

    @GetMapping("/{customerId}/bookings")
    public ResponseEntity<List<BookingDTO>> getBookingsForCustomer(@PathVariable @Positive Long customerId) {
        List<BookingDTO> bookings = bookingService.findBookingsByCustomerId(customerId);
        return ResponseEntity.ok(bookings);
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> addCustomer(@Valid @RequestBody CustomerDTO customer) {
        CustomerDTO savedCustomer = customerService.saveCustomer(customer);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable @Positive Long id,
                                                      @Valid @RequestBody CustomerDTO customer) {
        CustomerDTO updatedCustomer = customerService.updateCustomer(id, customer);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteCustomer(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(customerService.deleteCustomer(id));
    }
}
