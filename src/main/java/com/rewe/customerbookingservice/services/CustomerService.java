package com.rewe.customerbookingservice.services;

import com.rewe.customerbookingservice.dtos.CustomerDTO;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<CustomerDTO> findAllCustomers();

    Optional<CustomerDTO> findCustomerById(Long id);

    CustomerDTO saveCustomer(CustomerDTO customer);

    CustomerDTO updateCustomer(Long id, CustomerDTO customerDetails);

    boolean deleteCustomer(Long id);
}
