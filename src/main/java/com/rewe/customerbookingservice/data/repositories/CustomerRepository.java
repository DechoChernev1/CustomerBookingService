package com.rewe.customerbookingservice.data.repositories;

import com.rewe.customerbookingservice.data.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
