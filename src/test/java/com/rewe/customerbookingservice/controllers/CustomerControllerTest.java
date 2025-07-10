package com.rewe.customerbookingservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rewe.customerbookingservice.dtos.BookingDTO;
import com.rewe.customerbookingservice.dtos.CustomerDTO;
import com.rewe.customerbookingservice.services.impl.BookingServiceImpl;
import com.rewe.customerbookingservice.services.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerTest {

    @MockitoBean
    private CustomerServiceImpl customerService;

    @MockitoBean
    private BookingServiceImpl bookingService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomerDTO customerDTO;

    private BookingDTO bookingDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("Customer Name");
        customerDTO.setEmail("asd@asd.com");
        customerDTO.setAge(21);

        bookingDTO = new BookingDTO();
        bookingDTO.setId(1L);
        bookingDTO.setTitle("Booking Title");
        bookingDTO.setCustomer(customerDTO);
    }

    @Test
    void testAddCustomer() throws Exception {
        when(customerService.saveCustomer(any(CustomerDTO.class))).thenReturn(customerDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/customers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(customerDTO.getId()), Long.class));
    }

    @Test
    void testAddCustomerWithShortName() throws Exception {
        customerDTO.setName("Cu");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/customers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Constraint Violation Exception"), String.class));
    }

    @Test
    void testAddCustomerWithWrongAge() throws Exception {
        customerDTO.setAge(1);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/customers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Constraint Violation Exception"), String.class));
    }

    @Test
    void testAddCustomerWithWrongEmail() throws Exception {
        customerDTO.setEmail("Cucu");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/customers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Constraint Violation Exception"), String.class));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        when(customerService.updateCustomer(eq(1L), any(CustomerDTO.class))).thenReturn(customerDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/customers/{id}", customerDTO.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(customerDTO.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(customerDTO.getName()), String.class));
    }

    @Test
    void testDeleteCustomer() throws Exception {
        when(customerService.deleteCustomer(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/customers/{id}", customerDTO.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }


    @Test
    void testGetBookingsForCustomer() throws Exception {
        when(bookingService.findBookingsByCustomerId(1L)).thenReturn(Collections.singletonList(bookingDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/customers/{id}/bookings", customerDTO.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]id", is(bookingDTO.getId()), Long.class));
    }
}
