package com.rewe.customerbookingservice.integration.controllers;

import com.rewe.customerbookingservice.CustomerBookingServiceApplication;
import com.rewe.customerbookingservice.data.entities.Booking;
import com.rewe.customerbookingservice.data.entities.Brand;
import com.rewe.customerbookingservice.data.entities.Customer;
import com.rewe.customerbookingservice.data.repositories.BookingRepository;
import com.rewe.customerbookingservice.data.repositories.BrandRepository;
import com.rewe.customerbookingservice.data.repositories.CustomerRepository;
import com.rewe.customerbookingservice.dtos.BookingDTO;
import com.rewe.customerbookingservice.services.BookingService;
import com.rewe.customerbookingservice.services.BrandService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = CustomerBookingServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookingControllerIntegrationTest {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private TestRestTemplate restTemplate;

    private Customer testCustomer;
    private Brand testBrand;

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BrandRepository brandRepository;

    @BeforeEach
    void setupTestEntities() {
        // Set up test customer
        var testCustomer = new Customer();
        testCustomer.setName("Test Customer");
        testCustomer = customerRepository.save(testCustomer);
        this.testCustomer = testCustomer;

        // Set up test brand
        var testBrand = new Brand();
        testBrand.setName("Test Brand");
        testBrand = brandRepository.save(testBrand);
        this.testBrand = testBrand;
    }

    @AfterEach
    void cleanupTestEntities() {
        bookingRepository.deleteAll();
        customerRepository.deleteAll();
        brandRepository.deleteAll();
    }

    @Test
    void testAddBooking() throws URISyntaxException {
        URI uri = new URI("http://localhost:" + randomServerPort + "/api/bookings");

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setTitle("New Booking");

        ResponseEntity<BookingDTO> responseEntity = restTemplate.postForEntity(uri, bookingDTO, BookingDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        var responseBody = responseEntity.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getTitle()).isEqualTo("New Booking");

        Optional<Booking> booking = bookingRepository.findById(responseBody.getId());
        assertThat(booking).isPresent();
        assertThat(booking.get().getTitle()).isEqualTo("New Booking");
    }

    @Test
    void testGetBookingById() throws URISyntaxException {
        Booking booking = new Booking();
        booking.setTitle("Test Booking");
        Booking savedBooking = bookingRepository.save(booking);

        URI uri = new URI("http://localhost:" + randomServerPort + "/api/bookings/" + savedBooking.getId());

        ResponseEntity<BookingDTO> responseEntity = restTemplate.getForEntity(uri, BookingDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getTitle()).isEqualTo("Test Booking");

        Optional<Booking> bookingResult = bookingRepository.findById(savedBooking.getId());
        assertThat(bookingResult).isPresent();
        assertThat(bookingResult.get().getTitle()).isEqualTo("Test Booking");
    }

    @Test
    void testUpdateBooking() throws URISyntaxException {
        // Create a new booking for the test
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setTitle("Initial Booking");

        Booking booking = new Booking();
        booking.setTitle("Test Booking");
        Booking savedBooking = bookingRepository.save(booking);

        URI uri = new URI("http://localhost:" + randomServerPort + "/api/bookings/" + savedBooking.getId());

        bookingDTO.setTitle("Updated Booking");

        restTemplate.put(uri, bookingDTO);

        Optional<Booking> bookingResult = bookingRepository.findById(savedBooking.getId());
        assertThat(bookingResult).isPresent();
        assertThat(bookingResult.get().getTitle()).isEqualTo("Updated Booking");
    }

    @Test
    void testDeleteBooking() throws URISyntaxException {
        Booking booking = new Booking();
        booking.setTitle("Test Booking");
        Booking savedBooking = bookingRepository.save(booking);

        URI uri = new URI("http://localhost:" + randomServerPort + "/api/bookings/" + savedBooking.getId());

        restTemplate.delete(uri);

        Optional<Booking> bookingResult = bookingRepository.findById(savedBooking.getId());
        assertThat(bookingResult).isNotPresent();
    }
}

