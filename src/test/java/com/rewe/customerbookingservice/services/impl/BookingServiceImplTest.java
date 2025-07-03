package com.rewe.customerbookingservice.services.impl;

import com.rewe.customerbookingservice.data.entities.Booking;
import com.rewe.customerbookingservice.data.repositories.BookingRepository;
import com.rewe.customerbookingservice.dtos.BookingDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookingServiceImplTest {

    private final BookingRepository bookingRepository = mock(BookingRepository.class);
    private final ModelMapper modelMapper = new ModelMapper();
    private final BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, modelMapper);

    private Booking booking;
    private BookingDTO bookingDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        booking = new Booking();
        booking.setId(1L);
        booking.setTitle("Booking A");
        bookingDTO = modelMapper.map(booking, BookingDTO.class);
    }

    @Test
    void saveBooking_shouldReturnSavedBookingDTO_whenBookingIsValid() {
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDTO result = bookingService.saveBooking(bookingDTO);

        assertNotNull(result);
        assertEquals(bookingDTO.getId(), result.getId());
        assertEquals(bookingDTO.getTitle(), result.getTitle());
    }

    @Test
    void findBookingById_shouldReturnBookingDTO_whenBookingExists() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        Optional<BookingDTO> result = bookingService.findBookingById(1L);

        assertTrue(result.isPresent());
        assertEquals(bookingDTO.getId(), result.get().getId());
    }

    @Test
    void deleteBooking_shouldReturnTrue_whenBookingIsDeleted() {
        when(bookingRepository.existsById(1L)).thenReturn(false);

        boolean result = bookingService.deleteBooking(1L);

        assertTrue(result);
    }

    @Test
    void findAllBookings_shouldReturnListOfBookingDTOs() {
        when(bookingRepository.findAll()).thenReturn(List.of(booking));

        List<BookingDTO> result = bookingService.findAllBookings();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bookingDTO.getId(), result.get(0).getId());
    }

    @Test
    void updateBooking_shouldReturnUpdatedBookingDTO_whenBookingExists() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDTO updatedDetails = new BookingDTO();
        updatedDetails.setTitle("Booking B");

        BookingDTO result = bookingService.updateBooking(1L, updatedDetails);

        assertNotNull(result);
        assertEquals("Booking B", result.getTitle());
    }
}
