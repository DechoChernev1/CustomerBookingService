package com.rewe.customerbookingservice.controllers;

import com.rewe.customerbookingservice.dtos.CustomerDTO;
import com.rewe.customerbookingservice.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("Customer Name");
    }

    @Test
    void testAddCustomer() {
        when(customerService.saveCustomer(any(CustomerDTO.class))).thenReturn(customerDTO);

        ResponseEntity<CustomerDTO> response = customerController.addCustomer(customerDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(customerDTO, response.getBody());
    }

    @Test
    void testUpdateCustomer() {
        when(customerService.updateCustomer(eq(1L), any(CustomerDTO.class))).thenReturn(customerDTO);

        ResponseEntity<CustomerDTO> response = customerController.updateCustomer(1L, customerDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customerDTO, response.getBody());
    }

    @Test
    void testDeleteCustomer() {
        when(customerService.deleteCustomer(1L)).thenReturn(true);

        ResponseEntity<Void> response = customerController.deleteCustomer(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
