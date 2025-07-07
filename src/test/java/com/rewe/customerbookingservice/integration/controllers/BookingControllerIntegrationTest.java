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

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BrandService brandService;

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

    @Test
    void testGetBookingsForCustomer() throws URISyntaxException {
        Booking booking1 = new Booking();
        booking1.setTitle("Test Booking1");
        booking1.setCustomer(testCustomer);
        Booking savedBooking1 = bookingRepository.save(booking1);
        Booking booking2 = new Booking();
        booking2.setTitle("Test Booking2");
        booking2.setCustomer(testCustomer);
        Booking savedBooking2 = bookingRepository.save(booking2);

        URI uri = new URI("http://localhost:" + randomServerPort + "/api/bookings/customer/" + testCustomer.getId());

        ResponseEntity<List<BookingDTO>> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        // Extract the list from the response entity
        List<BookingDTO> objectList = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().size()).isEqualTo(2);

        List<Booking> bookings = bookingRepository.findByCustomer_Id(testCustomer.getId());
        assertThat(bookings).hasSize(2);
    }

    @Test
    void testGetBookingsByBrand() throws URISyntaxException {
        var testBrand = new Brand();
        testBrand.setName("Test Brand");
        testBrand = brandRepository.save(testBrand);

        Booking booking = new Booking();
        booking.setTitle("Test Booking");
        booking.setBrand(testBrand);
        Booking savedBooking = bookingRepository.save(booking);

        // Assuming brand already has a booking associated
        URI uri = new URI("http://localhost:" + randomServerPort + "/api/bookings/brand/" + testBrand.getId());

        ResponseEntity<List> responseEntity = restTemplate.getForEntity(uri, List.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().size()).isEqualTo(1);

        List<Booking> bookings = bookingRepository.findByBrand_Id(testBrand.getId());
        assertThat(bookings).hasSize(1);
    }

    @Test
    void testAddBrandToBooking() throws URISyntaxException {
        Booking booking = new Booking();
        booking.setTitle("Booking For Brand Test");
        Booking savedBooking = bookingRepository.save(booking);

        URI uri = new URI("http://localhost:" + randomServerPort + "/api/bookings/addBrand/" + savedBooking.getId() + "/" + testBrand.getId());

        restTemplate.put(uri, null);

        ResponseEntity<BookingDTO> responseEntity = restTemplate.getForEntity(new URI("http://localhost:" + randomServerPort + "/api/bookings/" + savedBooking.getId()), BookingDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getBrand().getId()).isEqualTo(testBrand.getId());

        List<Booking> bookings = bookingRepository.findByBrand_Id(testBrand.getId());
        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getTitle()).isEqualTo("Booking For Brand Test");
    }
}

