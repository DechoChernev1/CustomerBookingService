package com.rewe.customerbookingservice.integration.services;

import com.rewe.customerbookingservice.dtos.BookingDTO;
import com.rewe.customerbookingservice.services.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookingServiceImplIntegrationTest {

    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
            .withDatabaseName("integration-tests-db")
            .withUsername("test_user")
            .withPassword("test_user_pass");

    @Autowired
    private BookingService bookingService;

    @Test
    void saveBooking_shouldSaveAndReturnBookingDTO() {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setTitle("Booking A");

        BookingDTO savedBooking = bookingService.saveBooking(bookingDTO);

        assertThat(savedBooking).isNotNull();
        assertThat(savedBooking.getTitle()).isEqualTo("Booking A");
    }

    @Test
    void findBookingById_shouldReturnBookingDTO_whenBookingExists() {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setTitle("Booking B");
        BookingDTO savedBooking = bookingService.saveBooking(bookingDTO);

        Optional<BookingDTO> result = bookingService.findBookingById(savedBooking.getId());

        assertTrue(result.isPresent());
        assertEquals(savedBooking.getId(), result.get().getId());
    }

    @Test
    void findAllBookings_shouldReturnListOfBookingDTOs() {
        BookingDTO bookingDTO1 = new BookingDTO();
        bookingDTO1.setTitle("Booking X");
        BookingDTO bookingDTO2 = new BookingDTO();
        bookingDTO2.setTitle("Booking Y");

        bookingService.saveBooking(bookingDTO1);
        bookingService.saveBooking(bookingDTO2);

        List<BookingDTO> bookings = bookingService.findAllBookings();

        assertThat(bookings).hasSize(2);
    }

    @Test
    void deleteBooking_shouldRemoveBooking() {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setTitle("Booking Z");
        BookingDTO savedBooking = bookingService.saveBooking(bookingDTO);

        bookingService.deleteBooking(savedBooking.getId());

        Optional<BookingDTO> result = bookingService.findBookingById(savedBooking.getId());
        assertThat(result).isEmpty();
    }

    @Test
    void updateBooking_shouldUpdateAndReturnBookingDTO_whenBookingExists() {
        BookingDTO initial = new BookingDTO();
        initial.setTitle("Initial Booking");
        BookingDTO savedBooking = bookingService.saveBooking(initial);

        BookingDTO updateDetails = new BookingDTO();
        updateDetails.setTitle("Updated Booking");

        BookingDTO updatedBooking = bookingService.updateBooking(savedBooking.getId(), updateDetails);

        assertThat(updatedBooking).isNotNull();
        assertThat(updatedBooking.getTitle()).isEqualTo("Updated Booking");

        Optional<BookingDTO> fetchedBooking = bookingService.findBookingById(savedBooking.getId());
        assertThat(fetchedBooking).isPresent();
        assertThat(fetchedBooking.get().getTitle()).isEqualTo("Updated Booking");
    }
}
