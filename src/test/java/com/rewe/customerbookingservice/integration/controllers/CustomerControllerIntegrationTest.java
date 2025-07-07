package com.rewe.customerbookingservice.integration.controllers;

import com.rewe.customerbookingservice.CustomerBookingServiceApplication;
import com.rewe.customerbookingservice.dtos.CustomerDTO;
import com.rewe.customerbookingservice.services.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = CustomerBookingServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerIntegrationTest {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerService customerService;

    @Test
    void testAddCustomer() throws URISyntaxException {
        URI uri = new URI("http://localhost:" + randomServerPort + "/api/customers");

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("New Customer");
        customerDTO.setActive(true);
        customerDTO.setAge(22);
        customerDTO.setEmail("asd@asd.com");

        ResponseEntity<CustomerDTO> responseEntity = restTemplate.postForEntity(uri, customerDTO, CustomerDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getName()).isEqualTo("New Customer");
    }

    @Test
    void testUpdateCustomer() throws URISyntaxException {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("Customer to Update");
        customerDTO.setActive(true);
        customerDTO.setAge(22);
        customerDTO.setEmail("asd@asd.com");
        CustomerDTO savedCustomer = customerService.saveCustomer(customerDTO);

        URI uri = new URI("http://localhost:" + randomServerPort + "/api/customers/" + savedCustomer.getId());

        customerDTO.setName("Updated Customer");

        restTemplate.put(uri, customerDTO);

        Optional<CustomerDTO> updatedCustomer = customerService.findCustomerById(savedCustomer.getId());

        assertThat(updatedCustomer.get()).isNotNull();
        assertThat(updatedCustomer.get().getName()).isEqualTo("Updated Customer");
    }

    @Test
    void testDeleteCustomer() throws URISyntaxException {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("Customer To Delete");
        CustomerDTO savedCustomer = customerService.saveCustomer(customerDTO);

        URI uri = new URI("http://localhost:" + randomServerPort + "/api/customers/" + savedCustomer.getId());

        restTemplate.delete(uri);

        Optional<CustomerDTO> updatedCustomer = customerService.findCustomerById(savedCustomer.getId());

        assertThat(updatedCustomer.isPresent()).isFalse();
    }
}
