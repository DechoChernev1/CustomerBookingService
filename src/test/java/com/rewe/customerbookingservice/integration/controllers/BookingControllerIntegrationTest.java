package com.rewe.customerbookingservice.integration.controllers;

import com.rewe.customerbookingservice.CustomerBookingServiceApplication;
import com.rewe.customerbookingservice.dtos.BookingDTO;
import com.rewe.customerbookingservice.dtos.BrandDTO;
import com.rewe.customerbookingservice.dtos.CustomerDTO;
import com.rewe.customerbookingservice.services.BookingService;
import com.rewe.customerbookingservice.services.BrandService;
import com.rewe.customerbookingservice.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = CustomerBookingServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookingControllerIntegrationTest {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BrandService brandService;

    private Long testCustomerId;
    private Long testBrandId;

    @BeforeEach
    void setupTestEntities() {
        // Set up test customer
        var testCustomer = new CustomerDTO();
        testCustomer.setName("Test Customer");
        testCustomer = customerService.saveCustomer(testCustomer);
        this.testCustomerId = testCustomer.getId();

        // Set up test brand
        var testBrand = new BrandDTO();
        testBrand.setName("Test Brand");
        testBrand = brandService.saveBrand(testBrand);
        this.testBrandId = testBrand.getId();
    }

    @Test
    void testAddBooking() throws URISyntaxException {
        URI uri = new URI("http://localhost:" + randomServerPort + "/api/bookings");

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setTitle("New Booking");

        ResponseEntity<BookingDTO> responseEntity = restTemplate.postForEntity(uri, bookingDTO, BookingDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getTitle()).isEqualTo("New Booking");
    }

    @Test
    void testGetBookingById() throws URISyntaxException {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setTitle("Test Booking");
        BookingDTO savedBooking = bookingService.saveBooking(bookingDTO);

        URI uri = new URI("http://localhost:" + randomServerPort + "/api/bookings/" + savedBooking.getId());

        ResponseEntity<BookingDTO> responseEntity = restTemplate.getForEntity(uri, BookingDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getTitle()).isEqualTo("Test Booking");
    }

    @Test
    void testUpdateBooking() throws URISyntaxException {
        // Create a new booking for the test
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setTitle("Initial Booking");
        BookingDTO savedBooking = bookingService.saveBooking(bookingDTO);

        URI uri = new URI("http://localhost:" + randomServerPort + "/api/bookings/" + savedBooking.getId());

        bookingDTO.setTitle("Updated Booking");

        restTemplate.put(uri, bookingDTO);

        ResponseEntity<BookingDTO> responseEntity = restTemplate.getForEntity(uri, BookingDTO.class);

        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getTitle()).isEqualTo("Updated Booking");
    }

    @Test
    void testDeleteBooking() throws URISyntaxException {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setTitle("Booking To Delete");
        BookingDTO savedBooking = bookingService.saveBooking(bookingDTO);

        URI uri = new URI("http://localhost:" + randomServerPort + "/api/bookings/" + savedBooking.getId());

        restTemplate.delete(uri);

        ResponseEntity<BookingDTO> responseEntity = restTemplate.getForEntity(uri, BookingDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetBookingsForCustomer() throws URISyntaxException {
        // Assuming customer already has a booking associated
        URI uri = new URI("http://localhost:" + randomServerPort + "/api/bookings/customer/" + testCustomerId);

        ResponseEntity<List> responseEntity = restTemplate.getForEntity(uri, List.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    void testGetBookingsByBrand() throws URISyntaxException {
        var testBrand = new BrandDTO();
        testBrand.setName("Test Brand");
        testBrand = brandService.saveBrand(testBrand);

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setTitle("Booking For Brand Test");
        bookingDTO.setBrand(testBrand);
        BookingDTO savedBooking = bookingService.saveBooking(bookingDTO);

        // Assuming brand already has a booking associated
        URI uri = new URI("http://localhost:" + randomServerPort + "/api/bookings/brand/" + testBrand.getId());

        ResponseEntity<List> responseEntity = restTemplate.getForEntity(uri, List.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    void testAddBrandToBooking() throws URISyntaxException {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setTitle("Booking For Brand Test");
        BookingDTO savedBooking = bookingService.saveBooking(bookingDTO);

        URI uri = new URI("http://localhost:" + randomServerPort + "/api/bookings/addBrand/" + savedBooking.getId() + "/" + testBrandId);

        restTemplate.put(uri, null);

        ResponseEntity<BookingDTO> responseEntity = restTemplate.getForEntity(new URI("http://localhost:" + randomServerPort + "/api/bookings/" + savedBooking.getId()), BookingDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getBrand().getId()).isEqualTo(testBrandId);
    }
}

