package com.rewe.customerbookingservice.services;

import com.rewe.customerbookingservice.dtos.CustomerDTO;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    CustomerDTO saveCustomer(CustomerDTO customer);

    CustomerDTO updateCustomer(Long id, CustomerDTO customerDetails);

    boolean deleteCustomer(Long id);
}
