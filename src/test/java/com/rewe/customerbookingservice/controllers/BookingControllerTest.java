package com.rewe.customerbookingservice.controllers;

import com.rewe.customerbookingservice.dtos.BookingDTO;
import com.rewe.customerbookingservice.dtos.BrandDTO;
import com.rewe.customerbookingservice.dtos.CustomerDTO;
import com.rewe.customerbookingservice.services.BookingService;
import com.rewe.customerbookingservice.services.BrandService;
import com.rewe.customerbookingservice.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @Mock
    private CustomerService customerService;

    @Mock
    private BrandService brandService;

    @InjectMocks
    private BookingController bookingController;

    private BookingDTO bookingDTO;
    private BrandDTO brandDTO;
    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("Customer Name");

        brandDTO = new BrandDTO();
        brandDTO.setId(1L);
        brandDTO.setName("Brand Name");

        bookingDTO = new BookingDTO();
        bookingDTO.setId(1L);
        bookingDTO.setTitle("Booking Title");
        bookingDTO.setCustomer(customerDTO);
        bookingDTO.setBrand(brandDTO);
    }

    @Test
    void testAddBooking() {
        when(bookingService.saveBooking(any(BookingDTO.class))).thenReturn(bookingDTO);

        ResponseEntity<BookingDTO> response = bookingController.addBooking(bookingDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(bookingDTO, response.getBody());
    }

    @Test
    void testGetBookingsForCustomer() {
        when(bookingService.findBookingsByCustomerId(1L)).thenReturn(List.of(bookingDTO));

        ResponseEntity<List<BookingDTO>> response = bookingController.getBookingsForCustomer(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(bookingDTO), response.getBody());
    }

    @Test
    void testGetBookingsByBrand() {
        when(bookingService.findBookingsByBrandId(1L)).thenReturn(Collections.singletonList(bookingDTO));

        ResponseEntity<List<BookingDTO>> response = bookingController.getBookingsByBrand(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains(bookingDTO));
    }

    @Test
    void testDeleteBooking() {
        when(bookingService.deleteBooking(1L)).thenReturn(true);

        ResponseEntity<Void> response = bookingController.deleteBooking(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAddBrandToBooking() {
        when(bookingService.findBookingById(1L)).thenReturn(Optional.of(bookingDTO));
        when(brandService.findBrandById(1L)).thenReturn(Optional.of(brandDTO));
        when(bookingService.updateBooking(1L, bookingDTO)).thenReturn(bookingDTO);

        ResponseEntity<BookingDTO> response = bookingController.addBrandToBooking(1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookingDTO, response.getBody());
    }

    @Test
    void testGetBookingById() {
        when(bookingService.findBookingById(1L)).thenReturn(Optional.of(bookingDTO));

        ResponseEntity<BookingDTO> response = bookingController.getBookingById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookingDTO, response.getBody());
    }

    @Test
    void testUpdateBooking() {
        when(bookingService.updateBooking(1L, bookingDTO)).thenReturn(bookingDTO);

        ResponseEntity<BookingDTO> response = bookingController.updateBooking(1L, bookingDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookingDTO, response.getBody());
    }
}