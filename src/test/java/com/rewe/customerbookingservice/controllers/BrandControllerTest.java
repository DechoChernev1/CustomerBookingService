package com.rewe.customerbookingservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rewe.customerbookingservice.dtos.BookingDTO;
import com.rewe.customerbookingservice.dtos.BrandDTO;
import com.rewe.customerbookingservice.services.BrandService;
import com.rewe.customerbookingservice.services.impl.BookingServiceImpl;
import com.rewe.customerbookingservice.services.impl.BrandServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BrandController.class)
class BrandControllerTest {

    @MockitoBean
    private BrandServiceImpl brandService;

    @MockitoBean
    private BookingServiceImpl bookingService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private BrandDTO brandDTO;
    private BookingDTO bookingDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        brandDTO = new BrandDTO();
        brandDTO.setId(1L);
        brandDTO.setName("Brand Name");

        bookingDTO = new BookingDTO();
        bookingDTO.setId(1L);
        bookingDTO.setTitle("Booking Title");
        bookingDTO.setBrand(brandDTO);
    }

    @Test
    void testAddBrand() throws Exception {
        when(brandService.saveBrand(any(BrandDTO.class))).thenReturn(brandDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/brands")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(brandDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(brandDTO.getId()), Long.class));
    }

    @Test
    void testAddBrandWithShortName() throws Exception {
        brandDTO.setName("aa");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/brands")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(brandDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Constraint Violation Exception"), String.class));
    }

    @Test
    void testUpdateBrand() throws Exception {
        when(brandService.updateBrand(eq(1L), any(BrandDTO.class))).thenReturn(brandDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/brands/{id}", brandDTO.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(brandDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(brandDTO.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(brandDTO.getName()), String.class));
    }

    @Test
    void testUpdateBrandWithShortName() throws Exception {
        brandDTO.setName("aa");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/brands/{id}", brandDTO.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(brandDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Constraint Violation Exception"), String.class));
    }

    @Test
    void testDeleteBrand() throws Exception {
        when(brandService.deleteBrand(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/brands/{id}", brandDTO.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testGetBookingsByBrand() throws Exception {
        when(bookingService.findBookingsByBrandId(1L)).thenReturn(Collections.singletonList(bookingDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/brands/{id}/bookings", brandDTO.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]id", is(bookingDTO.getId()), Long.class));
    }
}
