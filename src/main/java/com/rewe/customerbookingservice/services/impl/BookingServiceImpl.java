package com.rewe.customerbookingservice.services.impl;

import com.rewe.customerbookingservice.data.entities.Booking;
import com.rewe.customerbookingservice.data.entities.Brand;
import com.rewe.customerbookingservice.data.repositories.BookingRepository;
import com.rewe.customerbookingservice.dtos.BookingDTO;
import com.rewe.customerbookingservice.services.BookingService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, ModelMapper modelMapper) {
        this.bookingRepository = bookingRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<BookingDTO> findAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(booking -> modelMapper.map(booking, BookingDTO.class))
                .toList();
    }

    @Override
    public BookingDTO findBookingById(Long id) throws EntityNotFoundException {
        return bookingRepository.findById(id)
                .map(booking -> modelMapper.map(booking, BookingDTO.class))
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public BookingDTO saveBooking(BookingDTO bookingDTO) {
        Booking booking = modelMapper.map(bookingDTO, Booking.class);
        Booking savedBooking = bookingRepository.save(booking);
        return modelMapper.map(savedBooking, BookingDTO.class);
    }

    @Override
    public BookingDTO updateBooking(Long id, BookingDTO bookingDetails) throws EntityNotFoundException {
        Optional<Booking> existingBooking = bookingRepository.findById(id);
        if (existingBooking.isPresent()) {
            Booking bookingToUpdate = existingBooking.get();
            bookingToUpdate.setTitle(bookingDetails.getTitle());
            bookingToUpdate.setDescription(bookingDetails.getDescription());
            bookingToUpdate.setActive(bookingDetails.isActive());
            bookingToUpdate.setStartDate(bookingDetails.getStartDate());
            bookingToUpdate.setEndDate(bookingDetails.getEndDate());
            if (bookingDetails.getBrand() != null) {
                bookingToUpdate.setBrand(modelMapper.map(bookingDetails.getBrand(), Brand.class)); // Assuming you have the ID mapping set correctly
            }

            Booking updatedBooking = bookingRepository.save(bookingToUpdate);
            return modelMapper.map(updatedBooking, BookingDTO.class);
        }
        throw new EntityNotFoundException("Booking not found for id: " + id);
    }

    @Override
    public boolean deleteBooking(Long id) {
        bookingRepository.deleteById(id);
        return !bookingRepository.existsById(id);
    }

    @Override
    public List<BookingDTO> findBookingsByCustomerId(Long customerId) {
        List<BookingDTO> bookingDTOList =
                bookingRepository
                        .findByCustomerId(customerId)
                        .stream()
                        .map(booking -> modelMapper.map(booking, BookingDTO.class)).toList();

        return bookingDTOList;
    }

    @Override
    public List<BookingDTO> findBookingsByBrandId(Long brandId) {
        List<BookingDTO> bookingDTOList =
                bookingRepository
                        .findByBrandId(brandId)
                        .stream()
                        .map(booking -> modelMapper.map(booking, BookingDTO.class)).toList();

        return bookingDTOList;
    }
}
