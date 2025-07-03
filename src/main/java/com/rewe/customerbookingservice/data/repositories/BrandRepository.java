package com.rewe.customerbookingservice.data.repositories;

import com.rewe.customerbookingservice.data.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
}
