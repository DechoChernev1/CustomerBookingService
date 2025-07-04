package com.rewe.customerbookingservice.integration.services;

import com.rewe.customerbookingservice.dtos.CustomerDTO;
import com.rewe.customerbookingservice.services.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerServiceImplIntegrationTest {

    @Autowired
    private CustomerService customerService;

    @Test
    void saveCustomer_shouldSaveAndReturnCustomerDTO() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("Alice");
        customerDTO.setAge(25);
        customerDTO.setActive(true);
        CustomerDTO savedCustomer = customerService.saveCustomer(customerDTO);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getName()).isEqualTo("Alice");
    }

    @Test
    void findCustomerById_shouldReturnCustomerDTO_whenCustomerExists() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("Bob");

        CustomerDTO customerDTOSaved = customerService.saveCustomer(customerDTO);

        Optional<CustomerDTO> customer = customerService.findCustomerById(customerDTOSaved.getId());

        assertTrue(customer.isPresent());
        assertEquals(customerDTOSaved.getId(), customer.get().getId());
    }

    @Test
    void findAllCustomers_shouldReturnListOfCustomerDTOs() {
        CustomerDTO customerDTO1 = new CustomerDTO();
        customerDTO1.setName("Bob");
        CustomerDTO customerDTO2 = new CustomerDTO();
        customerDTO2.setName("Charlie");

        customerService.saveCustomer(customerDTO1);
        customerService.saveCustomer(customerDTO2);

        List<CustomerDTO> customers = customerService.findAllCustomers();

        assertThat(customers).hasSize(2);
    }

    @Test
    void deleteCustomer_shouldRemoveCustomer() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("Dave");
        CustomerDTO savedCustomer = customerService.saveCustomer(customerDTO);

        customerService.deleteCustomer(savedCustomer.getId());

        Optional<CustomerDTO> result = customerService.findCustomerById(savedCustomer.getId());
        assertThat(result).isEmpty();
    }

    @Test
    void updateCustomer_shouldUpdateAndReturnCustomerDTO_whenCustomerExists() {
        CustomerDTO initial = new CustomerDTO();
        initial.setName("Charlie");
        CustomerDTO savedCustomer = customerService.saveCustomer(initial);

        CustomerDTO updateDetails = new CustomerDTO();
        updateDetails.setName("Charlie Updated");

        CustomerDTO updatedCustomer = customerService.updateCustomer(savedCustomer.getId(), updateDetails);
        assertThat(updatedCustomer).isNotNull();
        assertThat(updatedCustomer.getName()).isEqualTo("Charlie Updated");

        Optional<CustomerDTO> fetchedCustomer = customerService.findCustomerById(savedCustomer.getId());
        assertThat(fetchedCustomer).isPresent();
        assertThat(fetchedCustomer.get().getName()).isEqualTo("Charlie Updated");
    }
}
