package com.rewe.customerbookingservice.integration.controllers;

import com.rewe.customerbookingservice.CustomerBookingServiceApplication;
import com.rewe.customerbookingservice.data.entities.Brand;
import com.rewe.customerbookingservice.data.repositories.BookingRepository;
import com.rewe.customerbookingservice.data.repositories.BrandRepository;
import com.rewe.customerbookingservice.data.repositories.CustomerRepository;
import com.rewe.customerbookingservice.dtos.BrandDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = CustomerBookingServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BrandControllerIntegrationTest {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @AfterEach
    void cleanupTestEntities() {
        bookingRepository.deleteAll();
        customerRepository.deleteAll();
        brandRepository.deleteAll();
    }

    @Test
    void testAddBrand() throws URISyntaxException {
        URI uri = new URI("http://localhost:" + randomServerPort + "/api/brands");

        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setName("New Brand");

        ResponseEntity<BrandDTO> responseEntity = restTemplate.postForEntity(uri, brandDTO, BrandDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getName()).isEqualTo("New Brand");

        Optional<Brand> brand = brandRepository.findById(responseEntity.getBody().getId());
        assertThat(brand.isPresent()).isTrue();
        assertThat(brand.get().getName()).isEqualTo("New Brand");
    }

    @Test
    void testUpdateBrand() throws URISyntaxException {
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setName("Brand to Update");

        Brand brand = new Brand();
        brand.setName("Brand to Update");
        Brand savedBrand = brandRepository.save(brand);

        URI uri = new URI("http://localhost:" + randomServerPort + "/api/brands/" + savedBrand.getId());

        brandDTO.setName("Updated Brand");

        ResponseEntity<BrandDTO> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.PUT,
                new HttpEntity<>(brandDTO),
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getName()).isEqualTo("Updated Brand");

        Optional<Brand> updatedBrand = brandRepository.findById(savedBrand.getId());

        assertThat(updatedBrand.get()).isNotNull();
        assertThat(updatedBrand.get().getName()).isEqualTo("Updated Brand");


    }

    @Test
    void testDeleteBrand() throws URISyntaxException {
        Brand brand = new Brand();
        brand.setName("Brand To Delete");
        Brand savedBrand = brandRepository.save(brand);

        URI uri = new URI("http://localhost:" + randomServerPort + "/api/brands/" + savedBrand.getId());

        ResponseEntity<BrandDTO> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNull();

        Optional<Brand> updatedBrand = brandRepository.findById(savedBrand.getId());
        assertThat(updatedBrand.isPresent()).isFalse();
    }
}
