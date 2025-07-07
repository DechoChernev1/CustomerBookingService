package com.rewe.customerbookingservice.integration.controllers;

import com.rewe.customerbookingservice.CustomerBookingServiceApplication;
import com.rewe.customerbookingservice.dtos.BrandDTO;
import com.rewe.customerbookingservice.services.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
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
    private BrandService brandService;

    @Test
    void testAddBrand() throws URISyntaxException {
        URI uri = new URI("http://localhost:" + randomServerPort + "/api/brands");

        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setName("New Brand");

        ResponseEntity<BrandDTO> responseEntity = restTemplate.postForEntity(uri, brandDTO, BrandDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getName()).isEqualTo("New Brand");
    }

    @Test
    void testUpdateBrand() throws URISyntaxException {
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setName("Brand to Update");
        BrandDTO savedBrand = brandService.saveBrand(brandDTO);

        URI uri = new URI("http://localhost:" + randomServerPort + "/api/brands/" + savedBrand.getId());

        brandDTO.setName("Updated Brand");

        restTemplate.put(uri, brandDTO);

        Optional<BrandDTO> updatedBrand = brandService.findBrandById(savedBrand.getId());

        assertThat(updatedBrand.get()).isNotNull();
        assertThat(updatedBrand.get().getName()).isEqualTo("Updated Brand");
    }

    @Test
    void testDeleteBrand() throws URISyntaxException {
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setName("Brand To Delete");
        BrandDTO savedBrand = brandService.saveBrand(brandDTO);

        URI uri = new URI("http://localhost:" + randomServerPort + "/api/brands/" + savedBrand.getId());

        restTemplate.delete(uri);

        Optional<BrandDTO> updatedBrand = brandService.findBrandById(savedBrand.getId());

        assertThat(updatedBrand.isPresent()).isFalse();
    }
}
