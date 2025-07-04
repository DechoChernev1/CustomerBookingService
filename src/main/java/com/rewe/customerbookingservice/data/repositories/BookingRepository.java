package com.rewe.customerbookingservice.data.repositories;

import com.rewe.customerbookingservice.data.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBrand_Id(Long brandId);
}