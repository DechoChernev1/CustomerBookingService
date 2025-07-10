package com.rewe.customerbookingservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rewe.customerbookingservice.dtos.BookingDTO;
import com.rewe.customerbookingservice.dtos.BrandDTO;
import com.rewe.customerbookingservice.dtos.CustomerDTO;
import com.rewe.customerbookingservice.services.BookingService;
import com.rewe.customerbookingservice.services.BrandService;
import com.rewe.customerbookingservice.services.CustomerService;
import com.rewe.customerbookingservice.services.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @MockitoBean
    private BookingServiceImpl bookingService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private BookingDTO bookingDTO;
    private BrandDTO brandDTO;
    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("Customer Name");
        customerDTO.setEmail("asd@asd.com");
        customerDTO.setAge(25);

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
    void testAddBooking() throws Exception {
        when(bookingService.saveBooking(any(BookingDTO.class))).thenReturn(bookingDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/bookings")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(bookingDTO.getId()), Long.class));
    }

    @Test
    void testAddBookingWithShortTitle() throws Exception {
        bookingDTO.setTitle("aa");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/bookings")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Constraint Violation Exception"), String.class));
    }

    @Test
    void testDeleteBooking() throws Exception {
        when(bookingService.deleteBooking(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/bookings/{id}", bookingDTO.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testGetBookingById() throws Exception {
        when(bookingService.findBookingById(1L)).thenReturn(bookingDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/bookings/{id}", bookingDTO.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDTO.getId()), Long.class));
    }

    @Test
    void testUpdateBooking() throws Exception {
        when(bookingService.updateBooking(1L, bookingDTO)).thenReturn(bookingDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/bookings/{id}", bookingDTO.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDTO.getId()), Long.class))
                .andExpect(jsonPath("$.title", is(bookingDTO.getTitle()), String.class));
    }

    @Test
    void testUpdateBookingWithShortTitle() throws Exception {
        bookingDTO.setTitle("aa");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/bookings/{id}", bookingDTO.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Constraint Violation Exception"), String.class));
    }
}