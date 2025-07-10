package com.rewe.customerbookingservice.integration.controllers;

import com.rewe.customerbookingservice.CustomerBookingServiceApplication;
import com.rewe.customerbookingservice.data.entities.Booking;
import com.rewe.customerbookingservice.data.entities.Customer;
import com.rewe.customerbookingservice.data.repositories.BookingRepository;
import com.rewe.customerbookingservice.data.repositories.BrandRepository;
import com.rewe.customerbookingservice.data.repositories.CustomerRepository;
import com.rewe.customerbookingservice.dtos.BookingDTO;
import com.rewe.customerbookingservice.dtos.CustomerDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = CustomerBookingServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerIntegrationTest {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ModelMapper modelMapper;

    @AfterEach
    void cleanupTestEntities() {
        bookingRepository.deleteAll();
        customerRepository.deleteAll();
        brandRepository.deleteAll();
    }

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

        Customer customer = customerRepository.findById(responseEntity.getBody().getId()).get();
        assertThat(customer.getName()).isEqualTo("New Customer");
    }

    @Test
    void testUpdateCustomer() throws URISyntaxException {
        Customer customer = new Customer();
        customer.setName("Customer to Update");
        customer.setActive(true);
        customer.setAge(22);
        customer.setEmail("asd@asd.com");
        Customer savedCustomer = customerRepository.save(customer);
        CustomerDTO customerDTO = modelMapper.map(savedCustomer, CustomerDTO.class);

        URI uri = new URI("http://localhost:" + randomServerPort + "/api/customers/" + savedCustomer.getId());

        customerDTO.setName("Updated Customer");

        ResponseEntity<CustomerDTO> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.PUT,
                new HttpEntity<>(customerDTO),
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getName()).isEqualTo("Updated Customer");

        Optional<Customer> updatedCustomer = customerRepository.findById(savedCustomer.getId());

        assertThat(updatedCustomer.get()).isNotNull();
        assertThat(updatedCustomer.get().getName()).isEqualTo("Updated Customer");
    }

    @Test
    void testDeleteCustomer() throws URISyntaxException {
        Customer customer = new Customer();
        customer.setName("Customer To Delete");
        customer.setActive(true);
        customer.setAge(22);
        customer.setEmail("asd@asd.com");
        Customer savedCustomer = customerRepository.save(customer);

        URI uri = new URI("http://localhost:" + randomServerPort + "/api/customers/" + savedCustomer.getId());

        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isTrue();

        Optional<Customer> updated = customerRepository.findById(savedCustomer.getId());
        assertThat(updated.isPresent()).isFalse();
    }

    @Test
    void testGetBookingsForCustomer() throws URISyntaxException {
        Customer customer = new Customer();
        customer.setName("Customer To Delete");
        customer.setActive(true);
        customer.setAge(22);
        customer.setEmail("asd@asd.com");
        Customer savedCustomer = customerRepository.save(customer);
        Booking booking1 = new Booking();
        booking1.setTitle("Test Booking1");
        booking1.setCustomer(savedCustomer);
        Booking savedBooking1 = bookingRepository.save(booking1);
        Booking booking2 = new Booking();
        booking2.setTitle("Test Booking2");
        booking2.setCustomer(savedCustomer);
        Booking savedBooking2 = bookingRepository.save(booking2);

        URI uri = new URI("http://localhost:" + randomServerPort + "/api/customers/" + savedCustomer.getId() + "/bookings");

        ResponseEntity<List<BookingDTO>> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().size()).isEqualTo(2);

        List<Booking> bookings = bookingRepository.findByCustomerId(savedCustomer.getId());
        assertThat(bookings).hasSize(2);
    }
}
