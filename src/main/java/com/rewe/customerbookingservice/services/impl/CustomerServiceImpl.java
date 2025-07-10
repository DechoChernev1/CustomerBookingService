package com.rewe.customerbookingservice.services.impl;

import com.rewe.customerbookingservice.data.entities.Customer;
import com.rewe.customerbookingservice.data.repositories.CustomerRepository;
import com.rewe.customerbookingservice.dtos.CustomerDTO;
import com.rewe.customerbookingservice.services.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customer) {
        Customer customerEntity = modelMapper.map(customer, Customer.class);
        Customer savedCustomer = customerRepository.save(customerEntity);
        return modelMapper.map(savedCustomer, CustomerDTO.class);
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDetails) throws EntityNotFoundException {
        Optional<Customer> existingCustomer = customerRepository.findById(id);
        if (existingCustomer.isPresent()) {
            Customer customerToUpdate = existingCustomer.get();
            customerToUpdate.setName(customerDetails.getName());
            customerToUpdate.setEmail(customerDetails.getEmail());
            customerToUpdate.setActive(customerDetails.isActive());
            customerToUpdate.setAge(customerDetails.getAge());
            Customer updatedCustomer = customerRepository.save(customerToUpdate);
            return modelMapper.map(updatedCustomer, CustomerDTO.class);
        }
        throw new EntityNotFoundException("Customer with id " + id + " not found");
    }

    @Override
    public boolean deleteCustomer(Long id) {
        customerRepository.deleteById(id);
        return !customerRepository.existsById(id);
    }
}
